package net.dadamalda.ocr_extra_drivers.mixin.ocr_extra_drivers;

import com.llamalad7.mixinextras.sugar.Local;
import li.cil.oc.Settings;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@Mixin(Relay.class)
public abstract class RelayBlockEntityMixin extends BlockEntity implements SatelliteNetwork.SatelliteNode {
    public RelayBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Unique
    private static final Method OCRExtraDrivers$enqueuePacket = OCRExtraDrivers$findEnqueuePacket();

    @Unique
    private static Method OCRExtraDrivers$findEnqueuePacket() {
        try {
            return Relay.class.getMethod("tryEnqueuePacket", Option.class, Packet.class);
        } catch (NoSuchMethodException e) {
            try {
                return Relay.class.getMethod("tryEnqueuePacketJava", Option.class, Packet.class);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Shadow
    public abstract boolean isRepeater();

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

    @Unique
    private void OCRExtraDrivers$checkLineOfSight() {
        if(!OCRExtraDrivers$dimensionsAbove.isEmpty()) {
            OCRExtraDrivers$canSeeAbove = SatelliteNetwork.canSee(level,
                    worldPosition.getCenter(),
                    true, SatelliteNetwork.SatelliteDirection.ABOVE);
        }
        if(!OCRExtraDrivers$dimensionsBelow.isEmpty()) {
            OCRExtraDrivers$canSeeBelow = SatelliteNetwork.canSee(level,
                    worldPosition.getCenter(),
                    true, SatelliteNetwork.SatelliteDirection.BELOW);
        }
        if(level == null) return;
        MinecraftServer server = level.getServer();
        if(server == null) return;
        OCRExtraDrivers$lastCheckedTick = server.getTickCount();
    }

    @Override
    public void receiveSatellitePacket(Packet packet, SatelliteNetwork.SatelliteDirection sentFrom) {
        if(level == null) return;
        MinecraftServer server = level.getServer();
        if(server == null) return;
        if(OCRExtraDrivers$lastCheckedTick+TICKS_BETWEEN_CHECKS < server.getTickCount()) OCRExtraDrivers$checkLineOfSight();
        if(
                (sentFrom == SatelliteNetwork.SatelliteDirection.ABOVE && OCRExtraDrivers$canSeeAbove) ||
                        (sentFrom == SatelliteNetwork.SatelliteDirection.BELOW && OCRExtraDrivers$canSeeBelow)
        ) {
            try {
                OCRExtraDrivers$enqueuePacket.invoke(this, Option.empty(), packet);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Inject(method = "relayPacket", at = @At("TAIL"))
    private void relayPacket(Option<Direction> sourceSide, Packet packet, CallbackInfo ci, @Local Function1<Double, Boolean> tryChangeBuffer) {
        if(!OCRExtraDrivers$hasSatelliteCard) return;
        if(!sourceSide.isDefined() && !isRepeater()) return;
        double cost = Settings.get().wirelessCostPerRange()[Tier.Two()] * SATELLITE_DISTANCE;
        if(!Settings.get().ignorePower() && !tryChangeBuffer.apply(-cost)) return;
        if(level == null) return;
        MinecraftServer server = level.getServer();
        if(server == null) return;
        if(OCRExtraDrivers$lastCheckedTick+TICKS_BETWEEN_CHECKS < server.getTickCount()) OCRExtraDrivers$checkLineOfSight();
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
        if(level == null) return;
        if(!stack.is(OEDItems.SATELLITE_CARD)) return;
        OCRExtraDrivers$hasSatelliteCard = true;
        SatelliteNetwork.add(this, level.dimension().location());
        OCRExtraDrivers$dimensionsAbove = SatelliteNetwork.getDimensionsAbove(level);
        OCRExtraDrivers$dimensionsBelow = SatelliteNetwork.getDimensionsBelow(level);
    }

    @Inject(method = "onItemRemoved", at = @At("TAIL"))
    private void onItemRemoved(int slot, ItemStack stack, CallbackInfo ci) {
        if(stack.is(OEDItems.SATELLITE_CARD)) return;
        OCRExtraDrivers$hasSatelliteCard = false;
        if(level == null) return;
        SatelliteNetwork.remove(this, level.dimension().location());
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
            if(level == null) return;
            OCRExtraDrivers$hasSatelliteCard = true;
            SatelliteNetwork.add(this, level.dimension().location());
            OCRExtraDrivers$dimensionsAbove = SatelliteNetwork.getDimensionsAbove(level);
            OCRExtraDrivers$dimensionsBelow = SatelliteNetwork.getDimensionsBelow(level);
        }
    }
}
