package net.dadamalda.ocr_extra_drivers.mixin.ocr_extra_drivers;

import com.llamalad7.mixinextras.sugar.Local;
import li.cil.oc.Settings;
import li.cil.oc.api.network.Component;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Packet;
import li.cil.oc.common.InventorySlots;
import li.cil.oc.common.Slot;
import li.cil.oc.common.Tier;
import li.cil.oc.common.blockentity.Relay;
import li.cil.oc.internal.scalalib.Function1;
import li.cil.oc.internal.scalalib.Option;
import net.dadamalda.ocr_extra_drivers.core.SatelliteNetwork;
import net.dadamalda.ocr_extra_drivers.init.OEDItems;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(Relay.class)
public abstract class RelayBlockEntityMixin implements SatelliteNetwork.SatelliteNode {
    @Shadow
    public abstract Level getEnvironmentLevel();

    @Shadow
    public abstract double xPosition();

    @Shadow
    public abstract double yPosition();

    @Shadow
    public abstract double zPosition();

    @Shadow
    public abstract boolean tryEnqueuePacket(Option<Direction> sourceSide, Packet packet);

    @Shadow
    public abstract boolean isRepeater();

    @Shadow
    public abstract Node node();

    @Shadow
    @Final
    private Component[] componentNodes;

    @Shadow
    public abstract ItemStack getItem(int slot);

    @Unique
    private final int TICKS_BETWEEN_CHECKS = 40;
    @Unique
    private final int SATELLITE_DISTANCE = 512;

    @Unique
    private boolean OCRExtraDrivers$hasSatelliteCard = false;
    @Unique
    private boolean OCRExtraDrivers$canSeeAbove = false;
    @Unique
    private boolean OCRExtraDrivers$canSeeBelow = false;
    @Unique
    private int OCRExtraDrivers$lastCheckedTick = Integer.MIN_VALUE;
    @Unique
    private List<ResourceLocation> OCRExtraDrivers$dimensionsAbove;
    @Unique
    private List<ResourceLocation> OCRExtraDrivers$dimensionsBelow;

    private void checkLineOfSight() {
        if(!OCRExtraDrivers$dimensionsAbove.isEmpty()) {
            OCRExtraDrivers$canSeeAbove = SatelliteNetwork.canSeeAbove(getEnvironmentLevel(),
                    xPosition(), yPosition(), zPosition(), true);
        }
        if(!OCRExtraDrivers$dimensionsBelow.isEmpty()) {
            OCRExtraDrivers$canSeeBelow = SatelliteNetwork.canSeeBelow(getEnvironmentLevel(),
                    xPosition(), yPosition(), zPosition(), true);
        }
        MinecraftServer server = getEnvironmentLevel().getServer();
        if(server == null) return;
        OCRExtraDrivers$lastCheckedTick = server.getTickCount();
    }

    @Override
    public void receiveSatellitePacket(Packet packet, SatelliteNetwork.SatelliteDirection sentFrom) {
        MinecraftServer server = getEnvironmentLevel().getServer();
        if(server == null) return;
        if(OCRExtraDrivers$lastCheckedTick+TICKS_BETWEEN_CHECKS < server.getTickCount()) checkLineOfSight();
        if(
                (sentFrom == SatelliteNetwork.SatelliteDirection.ABOVE && OCRExtraDrivers$canSeeAbove) ||
                        (sentFrom == SatelliteNetwork.SatelliteDirection.BELOW && OCRExtraDrivers$canSeeBelow)
        ) {
            tryEnqueuePacket(Option.empty(), packet);
        }
    }

    @Inject(method = "relayPacket", at = @At("TAIL"))
    private void relayPacket(Option<Direction> sourceSide, Packet packet, CallbackInfo ci, @Local Function1<Double, Boolean> tryChangeBuffer) {
        if(!OCRExtraDrivers$hasSatelliteCard) return;
        if(!sourceSide.isDefined() && !isRepeater()) return;
        double cost = Settings.get().wirelessCostPerRange()[Tier.Two()] * SATELLITE_DISTANCE;
        if(!Settings.get().ignorePower() && !tryChangeBuffer.apply(-cost)) return;

        MinecraftServer server = getEnvironmentLevel().getServer();
        if(server == null) return;
        if(OCRExtraDrivers$lastCheckedTick+TICKS_BETWEEN_CHECKS < server.getTickCount()) checkLineOfSight();
        if(OCRExtraDrivers$canSeeAbove) {
            for(ResourceLocation dimension : OCRExtraDrivers$dimensionsAbove) {
                SatelliteNetwork.receivePacketInDimension(packet, dimension, SatelliteNetwork.SatelliteDirection.BELOW);
            }
        }
        if(OCRExtraDrivers$canSeeBelow) {
            for(ResourceLocation dimension : OCRExtraDrivers$dimensionsBelow) {
                SatelliteNetwork.receivePacketInDimension(packet, dimension, SatelliteNetwork.SatelliteDirection.ABOVE);
            }
        }
    }

    @Inject(method = "updateLimits", at = @At("RETURN"))
    private void updateLimits(int slot, ItemStack stack, CallbackInfo ci) {
        // DriverItem driver = Driver.driverFor(stack);
        // if(!Objects.equals(driver.slot(stack), Slot.Card())) return;
        if(getEnvironmentLevel() == null) return;
        if(!stack.is(OEDItems.SATELLITE_CARD)) return;
        OCRExtraDrivers$hasSatelliteCard = true;
        SatelliteNetwork.add(this, getEnvironmentLevel().dimension().location());
        OCRExtraDrivers$dimensionsAbove = SatelliteNetwork.getDimensionsAbove(getEnvironmentLevel());
        OCRExtraDrivers$dimensionsBelow = SatelliteNetwork.getDimensionsBelow(getEnvironmentLevel());
    }

    @Inject(method = "onItemRemoved", at = @At("TAIL"))
    private void onItemRemoved(int slot, ItemStack stack, CallbackInfo ci) {
        // DriverItem driver = Driver.driverFor(stack);
        // if(!Objects.equals(driver.slot(stack), Slot.Card())) return;
        if(stack.is(OEDItems.SATELLITE_CARD)) return;
        OCRExtraDrivers$hasSatelliteCard = false;
        SatelliteNetwork.remove(this, getEnvironmentLevel().dimension().location());
    }

    @Inject(method = "canPlaceItem", at = @At("RETURN"), cancellable = true)
    private void canPlaceItem(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        InventorySlots.InventorySlot provided = InventorySlots.relay()[slot];
        if(!Objects.equals(provided.slot(), Slot.Card())) return;
        if(!stack.is(OEDItems.SATELLITE_CARD)) return;
        cir.setReturnValue(true);
    }

    @Inject(method = "onConnect", at = @At("RETURN"))
    private void onConnect(Node node, CallbackInfo ci) {
        if(getItem(3).is(OEDItems.SATELLITE_CARD)) {
            OCRExtraDrivers$hasSatelliteCard = true;
            SatelliteNetwork.add(this, getEnvironmentLevel().dimension().location());
            OCRExtraDrivers$dimensionsAbove = SatelliteNetwork.getDimensionsAbove(getEnvironmentLevel());
            OCRExtraDrivers$dimensionsBelow = SatelliteNetwork.getDimensionsBelow(getEnvironmentLevel());
        }
    }
}
