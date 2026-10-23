package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.common.blockentities.machines.OxygenLoaderBlockEntity;
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

public class DriverOxygenLoader extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return OxygenLoaderBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof OxygenLoaderBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<OxygenLoaderBlockEntity> implements NamedBlock {
        private OxygenLoaderBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "oxygen_loader";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(OxygenLoaderBlockEntity blockEntity) {
            super(blockEntity, "oxygen_loader");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy stored in the machine's buffer.")
        public Object[] getEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getStoredAmount()};
        }

        @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the machine's buffer.")
        public Object[] getMaxEnergyStored(Context context, Arguments args) {
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
            if(!itemStack.isEmpty() && itemStack.is(ModItems.OXYGEN_LOADER.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
