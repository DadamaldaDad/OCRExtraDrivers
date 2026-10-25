package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import com.st0x0ef.stellaris.common.blocks.entities.machines.PowerBankEntity;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Set;

public class DriverPowerBank extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return PowerBankEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof PowerBankEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<PowerBankEntity> implements NamedBlock {
        private PowerBankEntity blockEntity;

        @Override
        public String preferredName() {
            return "power_bank";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(PowerBankEntity blockEntity) {
            super(blockEntity, "power_bank");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy stored in the power bank.")
        public Object[] getEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getEnergy()};
        }

        @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the power bank.")
        public Object[] getMaxEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.getEnergy(Direction.UP).getMaxEnergy()};
        }
    }

    public static class Provider implements EnvironmentProvider {
        private final Set<Item> items = Set.of(
                ItemsRegistry.T1_POWER_BANK.get(),
                ItemsRegistry.T2_POWER_BANK.get(),
                ItemsRegistry.T3_POWER_BANK.get(),
                ItemsRegistry.T4_POWER_BANK.get()
        );

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && items.contains(itemStack.getItem())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
