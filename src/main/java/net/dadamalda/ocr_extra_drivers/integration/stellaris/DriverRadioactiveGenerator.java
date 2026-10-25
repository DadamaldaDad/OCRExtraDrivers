package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import com.st0x0ef.stellaris.common.blocks.entities.machines.RadioactiveGeneratorEntity;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
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

public class DriverRadioactiveGenerator extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return RadioactiveGeneratorEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof RadioactiveGeneratorEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<RadioactiveGeneratorEntity> implements NamedBlock {
        private RadioactiveGeneratorEntity blockEntity;

        @Override
        public String preferredName() {
            return "radioactive_generator";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(RadioactiveGeneratorEntity blockEntity) {
            super(blockEntity, "radioactive_generator");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy stored in the machine's buffer.")
        public Object[] getEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getEnergy()};
        }

        @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the machine's buffer.")
        public Object[] getMaxEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getMaxEnergy()};
        }

        @Callback(doc = "function():number -- The number of ticks that the generator will keep burning from the last consumed fuel.")
        public Object[] getBurnTime(Context context, Arguments args) {
            return new Object[]{blockEntity.dataAccess.get(0)};
        }

        @Callback(doc = "function():number -- The number of ticks that the currently burning fuel lasts in total.")
        public Object[] getCurrentItemBurnTime(Context context, Arguments args) {
            if(blockEntity.dataAccess.get(0) > 0) {
                return new Object[]{blockEntity.dataAccess.get(1)};
            }
            return new Object[]{0};
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ItemsRegistry.RADIOACTIVE_GENERATOR.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
