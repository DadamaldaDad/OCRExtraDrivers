package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import com.st0x0ef.stellaris.common.blocks.entities.machines.SolarPanelEntity;
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

public class DriverSolarPanel extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return SolarPanelEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof SolarPanelEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<SolarPanelEntity> implements NamedBlock {
        private SolarPanelEntity blockEntity;

        @Override
        public String preferredName() {
            return "solar_panel";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(SolarPanelEntity blockEntity) {
            super(blockEntity, "solar_panel");
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
            if(!itemStack.isEmpty() && itemStack.is(ItemsRegistry.SOLAR_PANEL.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
