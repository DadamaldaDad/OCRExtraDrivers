package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.common.blockentities.machines.GravityNormalizerBlockEntity;
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

public class DriverGravityNormalizer extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return GravityNormalizerBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof GravityNormalizerBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<GravityNormalizerBlockEntity> implements NamedBlock {
        private GravityNormalizerBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "gravity_normalizer";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(GravityNormalizerBlockEntity blockEntity) {
            super(blockEntity, "gravity_normalizer");
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

        @Callback(doc = "function():boolean -- Get the working area of the normalizer in blocks.")
        public Object[] getWorkingArea(Context context, Arguments args) {
            return new Object[]{blockEntity.distributedBlocksCount()};
        }

        @Callback(doc = "function():number -- Get the target gravity compared to earth's.")
        public Object[] getTargetGravity(Context context, Arguments args) {
            return new Object[]{blockEntity.targetGravity()};
        }

        @Callback(doc = "function(gravity:number):boolean -- Set the target gravity in multiples of earth's.")
        public Object[] setTargetGravity(Context context, Arguments args) {
            float previousGravity = blockEntity.targetGravity();
            if(args.isDouble(0) && args.checkDouble(0) >= 0 && args.checkDouble(0) <= 20/9.8 && args.checkDouble(0) != previousGravity) {
                blockEntity.setTargetGravity((float)args.checkDouble(0));
                blockEntity.setChanged();
                blockEntity.sync();
                return new Object[]{true};
            } else {
                return new Object[]{false};
            }
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ModItems.GRAVITY_NORMALIZER.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
