package net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers;

import li.cil.oc.Settings;
import li.cil.oc.api.Network;
import li.cil.oc.api.internal.Tiered;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import li.cil.oc.common.Tier;
import li.cil.oc.server.component.NetworkCard;
import net.dadamalda.ocr_extra_drivers.core.SatelliteNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.Map;

public class SatelliteCard extends NetworkCard implements SatelliteNetwork.SatelliteNode, Tiered {
    private final EnvironmentHost host;
    private final Component node = Network.newNode(this, Visibility.Network).
            withComponent("modem", Visibility.Neighbors).
            withConnector().
            create();

    private final int TICKS_BETWEEN_CHECKS = 40;

    private boolean canSeeAbove = false;
    private boolean canSeeBelow = false;
    private int lastCheckedTick = Integer.MIN_VALUE;
    private List<ResourceLocation> dimensionsAbove;
    private List<ResourceLocation> dimensionsBelow;

    public SatelliteCard(EnvironmentHost host) {
        super(host);
        this.host = host;
    }

    private void checkLineOfSight() {
        if(!dimensionsAbove.isEmpty()) {
            canSeeAbove = SatelliteNetwork.canSeeAbove(host.getEnvironmentLevel(),
                    host.xPosition(), host.yPosition(), host.zPosition());
        }
        if(!dimensionsBelow.isEmpty()) {
            canSeeBelow = SatelliteNetwork.canSeeBelow(host.getEnvironmentLevel(),
                    host.xPosition(), host.yPosition(), host.zPosition());
        }
        MinecraftServer server = host.getEnvironmentLevel().getServer();
        if(server == null) return;
        lastCheckedTick = server.getTickCount();
    }

    public void receiveSatellitePacket(Packet packet, SatelliteNetwork.SatelliteDirection sentFrom) {
        MinecraftServer server = host.getEnvironmentLevel().getServer();
        if(server == null) return;
        if(lastCheckedTick+TICKS_BETWEEN_CHECKS < server.getTickCount()) checkLineOfSight();
        if(
                (sentFrom == SatelliteNetwork.SatelliteDirection.ABOVE && canSeeAbove) ||
                        (sentFrom == SatelliteNetwork.SatelliteDirection.BELOW && canSeeBelow)
        ) {
            receivePacket(packet, 500.0, host);
        }
    }

    @Override
    public void doSend(Packet packet) {
        doTransmit(packet);
    }

    @Override
    public void doBroadcast(Packet packet) {
        doTransmit(packet);
    }

    private void doTransmit(Packet packet) {
        double cost = packet.size() / 32.0 + Settings.get().wirelessCostPerRange()[Tier.Two()] * 500.0;
        if(!(node() instanceof Connector connector)) return;
        if(!Settings.get().ignorePower() && !connector.tryChangeBuffer(-cost)) return;
        MinecraftServer server = host.getEnvironmentLevel().getServer();
        if(server == null) return;
        if(lastCheckedTick+TICKS_BETWEEN_CHECKS < server.getTickCount()) checkLineOfSight();
        if(canSeeAbove) {
            for(ResourceLocation dimension : dimensionsAbove) {
                SatelliteNetwork.receivePacketInDimension(packet, dimension, SatelliteNetwork.SatelliteDirection.BELOW);
            }
        }
        if(canSeeBelow) {
            for(ResourceLocation dimension : dimensionsBelow) {
                SatelliteNetwork.receivePacketInDimension(packet, dimension, SatelliteNetwork.SatelliteDirection.ABOVE);
            }
        }
    }

    @Override
    public Object[] isWireless(Context context, Arguments args) {
        return new Object[]{true};
    }

    @Override
    public Object[] isWired(Context context, Arguments args) {
        return new Object[]{false};
    }

    @Override
    public void onConnect(Node node) {
        super.onConnect(node);
        if(node == node()) {
            SatelliteNetwork.add(this, host.getEnvironmentLevel().dimension().location());
            dimensionsAbove = SatelliteNetwork.getDimensionsAbove(host.getEnvironmentLevel());
            dimensionsBelow = SatelliteNetwork.getDimensionsBelow(host.getEnvironmentLevel());
        }
    }

    @Override
    public void onDisconnect(Node node) {
        super.onDisconnect(node);
        if(node == node()) {
            SatelliteNetwork.remove(this, host.getEnvironmentLevel().dimension().location());
        }
    }

    @Override
    public Component node() {
        return this.node;
    }

    @Override
    public Map<String, String> getDeviceInfo() {
        return Map.of(
                DeviceAttribute.Class, DeviceClass.Network,
                DeviceAttribute.Description, "Satellite dish",
                DeviceAttribute.Vendor, "LunaLink",
                DeviceAttribute.Product, "D1SHS4T",
                DeviceAttribute.Capacity, Integer.toString(Settings.get().maxNetworkPacketSize()),
                DeviceAttribute.Size, Integer.toString(maxOpenPorts()),
                DeviceAttribute.Width, "500"
        );
    }

    @Override
    public int tier() {
        return Tier.Two();
    }
}
