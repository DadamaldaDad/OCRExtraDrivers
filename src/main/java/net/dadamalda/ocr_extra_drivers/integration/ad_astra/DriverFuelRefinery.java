package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.common.blockentities.machines.FuelRefineryBlockEntity;
import earth.terrarium.adastra.common.registry.ModItems;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DriverFuelRefinery extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return FuelRefineryBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof FuelRefineryBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<FuelRefineryBlockEntity> implements NamedBlock {
        private FuelRefineryBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "fuel_refinery";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(FuelRefineryBlockEntity blockEntity) {
            super(blockEntity, "fuel_refinery");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- The energy stored in the machine's buffer.")
        public Object[] getEnergy(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getStoredAmount()};
        }

        @Callback(doc = "function():number -- The size of the machine's energy buffer.")
        public Object[] getMaxEnergy(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getCapacity()};
        }

        @Callback(doc = "function():boolean -- Check whether the machine is processing.")
        public Object[] isProcessing(Context context, Arguments args) {
            return new Object[]{blockEntity.cookTimeTotal() > 0};
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ModItems.FUEL_REFINERY.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
