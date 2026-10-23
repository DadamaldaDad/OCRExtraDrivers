package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.EnergyMeterBlockEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DriverPowerMeter extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return EnergyMeterBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof EnergyMeterBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<EnergyMeterBlockEntity> implements NamedBlock {
        private EnergyMeterBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "current_transformer";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(EnergyMeterBlockEntity blockEntity) {
            super(blockEntity, "current_transformer");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy flowing through the current transformer.")
        public Object[] getEnergyFlow(Context context, Arguments args) {
            return new Object[]{blockEntity.getAveragePower()};
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(IEBlocks.Connectors.CURRENT_TRANSFORMER.asItem())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
