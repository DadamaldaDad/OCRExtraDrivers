package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.common.blockentities.machines.WaterPumpBlockEntity;
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

public class DriverWaterPump extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return WaterPumpBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof WaterPumpBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<WaterPumpBlockEntity> implements NamedBlock {
        private WaterPumpBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "water_pump";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(WaterPumpBlockEntity blockEntity) {
            super(blockEntity, "water_pump");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- The energy stored in the machine's buffer.")
        public Object[] getEnergy(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getStoredAmount()};
        }

        @Callback(doc = "function():number -- The size of the machine's buffer.")
        public Object[] getMaxEnergy(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getCapacity()};
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ModItems.WATER_PUMP.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
