package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.common.blockentities.machines.OxygenDistributorBlockEntity;
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

public class DriverOxygenDistributor extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return OxygenDistributorBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof OxygenDistributorBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<OxygenDistributorBlockEntity> implements NamedBlock {
        private OxygenDistributorBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "oxygen_distributor";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(OxygenDistributorBlockEntity blockEntity) {
            super(blockEntity, "oxygen_distributor");
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

        @Callback(doc = "function():boolean -- Get the working area of the distributor in blocks.")
        public Object[] getWorkingArea(Context context, Arguments args) {
            return new Object[]{blockEntity.distributedBlocksCount()};
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ModItems.OXYGEN_DISTRIBUTOR.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
