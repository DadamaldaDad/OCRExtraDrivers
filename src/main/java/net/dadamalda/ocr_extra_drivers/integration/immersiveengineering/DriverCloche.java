package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlockEntity;
import li.cil.oc.integration.ManagedBlockEntityEnvironment;
import net.dadamalda.ocr_extra_drivers.duck.ClocheBlockEntityMixinDuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DriverCloche extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return ClocheBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof ClocheBlockEntity be) {
            return new Environment(be.master());
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<ClocheBlockEntity> implements NamedBlock {
        private ClocheBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "garden_cloche";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(ClocheBlockEntity blockEntity) {
            super(blockEntity, "garden_cloche");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy stored in the machine.")
        public Object[] getEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.energyStorage.getEnergyStored()};
        }

        @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the machine.")
        public Object[] getMaxEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.energyStorage.getMaxEnergyStored()};
        }

        @Callback(doc = "function():number -- The number of ticks that the current seed has been growing for.")
        public Object[] getGrowTime(Context context, Arguments args) {
            if(!(blockEntity instanceof ClocheBlockEntityMixinDuck duck)) return new Object[]{null};
            return new Object[]{duck.OCRExtraDrivers$getGrowTime()};
        }

        @Callback(doc = "function():number -- The number of ticks that the current seed needs to grow.")
        public Object[] getTotalGrowTime(Context context, Arguments args) {
            ClocheRecipe recipe = blockEntity.cachedRecipe.get();
            if(recipe == null) return new Object[]{0};
            return new Object[]{recipe.time};
        }

        @Callback(doc = "function():number -- The growth speed multiplier of the fertilizer in the cloche.")
        public Object[] getGrowthModifier(Context context, Arguments args) {
            return new Object[]{blockEntity.fertilizerMod};
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(IEBlocks.MetalDevices.CLOCHE.asItem())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
