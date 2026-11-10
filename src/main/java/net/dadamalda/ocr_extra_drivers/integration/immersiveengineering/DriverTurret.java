package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.TurretBlockEntity;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedBlockEntity;
import li.cil.oc.integration.ManagedBlockEntityEnvironment;
import net.dadamalda.ocr_extra_drivers.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DriverTurret extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return TurretBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof TurretBlockEntity<?> be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<TurretBlockEntity<?>> implements NamedBlock {
        private TurretBlockEntity<?> blockEntity;

        @Override
        public String preferredName() {
            return "turret";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(TurretBlockEntity<?> blockEntity) {
            super(blockEntity, "turret");
            this.blockEntity = blockEntity;
        }

        @Callback(doc = "function():number -- Returns the amount of energy stored in the turret.")
        public Object[] getEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.energyStorage.getEnergyStored()};
        }

        @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the turret.")
        public Object[] getMaxEnergyStored(Context context, Arguments args) {
            return new Object[]{blockEntity.energyStorage.getMaxEnergyStored()};
        }

        @Callback(doc = "function():boolean -- Returns whether the turret is in blacklist mode.")
        public Object[] isBlacklist(Context context, Arguments args) {
            return new Object[]{!blockEntity.config.whitelist()};
        }

        @Callback(doc = "function():boolean -- Returns whether the turret is targeting animals.")
        public Object[] isTargetingAnimals(Context context, Arguments args) {
            return new Object[]{blockEntity.config.attackAnimals()};
        }

        @Callback(doc = "function():boolean -- Returns whether the turret is targeting neutrals.")
        public Object[] isTargetingNeutrals(Context context, Arguments args) {
            return new Object[]{blockEntity.config.attackNeutrals()};
        }

        @Callback(doc = "function():boolean -- Returns whether the turret is targeting players.")
        public Object[] isTargetingPlayers(Context context, Arguments args) {
            return new Object[]{blockEntity.config.attackNeutrals()};
        }

        @Callback(doc = "function():table -- Returns the target list of the turret.")
        public Object[] getTargetList(Context context, Arguments args) {
            return new Object[]{blockEntity.config.targetList()};
        }

        @Callback(doc = "function(blacklist: boolean) -- Sets whether the turret is in blacklist mode.")
        public Object[] setBlacklist(Context context, Arguments args) {
            if(!Config.computers_can_change_turret_targeting) return new Object[]{};
            if(!args.isBoolean(0)) throw new IllegalArgumentException("Argument must be a boolean");
            blockEntity.config = blockEntity.config.withWhitelist(!args.checkBoolean(0));
            return new Object[]{};
        }

        @Callback(doc = "function(target: boolean) -- Sets whether the turret should target animals.")
        public Object[] setTargetingAnimals(Context context, Arguments args) {
            if(!Config.computers_can_change_turret_targeting) return new Object[]{};
            if(!args.isBoolean(0)) throw new IllegalArgumentException("Argument must be a boolean");
            blockEntity.config = blockEntity.config.withAttackAnimals(args.checkBoolean(0));
            return new Object[]{};
        }

        @Callback(doc = "function(target: boolean) -- Sets whether the turret should target neutrals.")
        public Object[] setTargetingNeutrals(Context context, Arguments args) {
            if(!Config.computers_can_change_turret_targeting) return new Object[]{};
            if(!args.isBoolean(0)) throw new IllegalArgumentException("Argument must be a boolean");
            blockEntity.config = blockEntity.config.withAttackNeutrals(args.checkBoolean(0));
            return new Object[]{};
        }

        @Callback(doc = "function(target: boolean) -- Sets whether the turret should target players.")
        public Object[] setTargetingPlayers(Context context, Arguments args) {
            if(!Config.computers_can_change_turret_targeting) return new Object[]{};
            if(!args.isBoolean(0)) throw new IllegalArgumentException("Argument must be a boolean");
            blockEntity.config = blockEntity.config.withAttackPlayers(args.checkBoolean(0));
            return new Object[]{};
        }

        @Callback(doc = "function(list: table) -- Set the list of names the turret should target.")
        public Object[] setTargetList(Context context, Arguments args) {
            if(!Config.computers_can_change_turret_targeting) return new Object[]{};
            if(!args.isTable(0)) throw new IllegalArgumentException("Argument must be a table of strings");
            List<String> targetList = new ArrayList<>();
            for(Object value : args.checkTable(0).values()) {
                if(!(value instanceof String string)) throw new IllegalArgumentException("Argument must be a table of strings");
                targetList.add(string);
            }
            blockEntity.config = blockEntity.config.withTargetList(targetList);
            return new Object[]{};
        }

        @Callback(doc = "function():boolean -- Returns whether the computer can change the targeting of the turret. This depends on a server config option.")
        public Object[] canChangeTargeting(Context context, Arguments args) {
            return new Object[]{Config.computers_can_change_turret_targeting};
        }
    }

    public static class Provider implements EnvironmentProvider {
        private final Set<Item> items = Set.of(
                IEBlocks.MetalDevices.TURRET_CHEM.asItem(),
                IEBlocks.MetalDevices.TURRET_GUN.asItem()
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
