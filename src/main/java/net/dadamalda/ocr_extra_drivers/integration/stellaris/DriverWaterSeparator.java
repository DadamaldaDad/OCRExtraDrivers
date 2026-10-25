package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import com.st0x0ef.stellaris.common.blocks.entities.machines.WaterSeparatorBlockEntity;
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

public class DriverWaterSeparator extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return WaterSeparatorBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof WaterSeparatorBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<WaterSeparatorBlockEntity> implements NamedBlock {
        private WaterSeparatorBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "water_separator";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(WaterSeparatorBlockEntity blockEntity) {
            super(blockEntity, "water_separator");
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
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ItemsRegistry.WATER_SEPARATOR.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
