package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.CapacitorBlockEntity;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlockEntity;
import li.cil.oc.integration.ManagedBlockEntityEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Set;

public class DriverAccumulator extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return CapacitorBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof CapacitorBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<CapacitorBlockEntity> implements NamedBlock {
        private CapacitorBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "accumulator";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(CapacitorBlockEntity blockEntity) {
            super(blockEntity, "accumulator");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy stored in the accumulator.")
        public Object[] getEnergyStored(Context context, Arguments args) {
            Level level = blockEntity.getLevel();
            if(level == null) return new Object[]{null};
            IEnergyStorage capability = level.getCapability(Capabilities.EnergyStorage.BLOCK, blockEntity.getBlockPos(), Direction.UP);
            if(capability == null) return new Object[]{null};
            return new Object[]{capability.getEnergyStored()};
        }

        @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the accumulator.")
        public Object[] getMaxEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.getMaxStorage()};
        }
    }

    public static class Provider implements EnvironmentProvider {
        private final Set<Item> items = Set.of(
                IEBlocks.MetalDevices.CAPACITOR_LV.asItem(),
                IEBlocks.MetalDevices.CAPACITOR_MV.asItem(),
                IEBlocks.MetalDevices.CAPACITOR_HV.asItem(),
                IEBlocks.MetalDevices.CAPACITOR_CREATIVE.asItem()
        );

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && items.contains(itemStack.getItem())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
