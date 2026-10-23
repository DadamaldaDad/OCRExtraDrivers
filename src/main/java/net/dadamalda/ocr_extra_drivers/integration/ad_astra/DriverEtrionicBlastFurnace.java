package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.common.blockentities.machines.EtrionicBlastFurnaceBlockEntity;
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

public class DriverEtrionicBlastFurnace extends DriverSidedBlockEntity {
    @Override
    public Class<?> getBlockEntityClass() {
        return EtrionicBlastFurnaceBlockEntity.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(Level level, BlockPos blockPos, Direction direction) {
        if(level.getBlockEntity(blockPos) instanceof EtrionicBlastFurnaceBlockEntity be) {
            return new Environment(be);
        } else {
            return null;
        }
    }

    public static class Environment extends ManagedBlockEntityEnvironment<EtrionicBlastFurnaceBlockEntity> implements NamedBlock {
        private EtrionicBlastFurnaceBlockEntity blockEntity;

        @Override
        public String preferredName() {
            return "etrionic_blast_furnace";
        }

        @Override
        public int priority() {
            return 0;
        }

        public Environment(EtrionicBlastFurnaceBlockEntity blockEntity) {
            super(blockEntity, "etrionic_blast_furnace");
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

        @Callback(doc = "function():number -- The number of ticks that the current item has been cooking for.")
        public Object[] getCookTime(Context context, Arguments args) {
            return new Object[]{blockEntity.cookTime()};
        }

        @Callback(doc = "function():number -- The number of ticks that the current item needs to cook.")
        public Object[] getTotalCookTime(Context context, Arguments args) {
            return new Object[]{blockEntity.cookTimeTotal()};
        }

        @Callback(doc = "function():string -- Get the working mode of the etrionic blast furnace")
        public Object[] getMode(Context context, Arguments args) {
            return switch(blockEntity.mode()) {
                case EtrionicBlastFurnaceBlockEntity.Mode.BLASTING -> new Object[]{"blasting"};
                case EtrionicBlastFurnaceBlockEntity.Mode.ALLOYING -> new Object[]{"alloying"};
            };
        }

        @Callback(doc = "function(mode:string):boolean -- Set the working mode of the etrionic blast furnace")
        public Object[] setMode(Context context, Arguments args) {
            if(!args.isString(0)) return new Object[]{false};
            switch(args.checkString(0)) {
                case "blasting":
                    if(blockEntity.mode() != EtrionicBlastFurnaceBlockEntity.Mode.BLASTING) {
                        blockEntity.setMode(EtrionicBlastFurnaceBlockEntity.Mode.BLASTING);
                        blockEntity.setChanged();
                        blockEntity.sync();
                        return new Object[]{true};
                    } else {
                        return new Object[]{false};
                    }
                case "alloying":
                    if(blockEntity.mode() != EtrionicBlastFurnaceBlockEntity.Mode.ALLOYING) {
                        blockEntity.setMode(EtrionicBlastFurnaceBlockEntity.Mode.ALLOYING);
                        blockEntity.setChanged();
                        blockEntity.sync();
                        return new Object[]{true};
                    } else {
                        return new Object[]{false};
                    }
                default:
                    throw new IllegalArgumentException("Mode must be either blasting or alloying");
            }
        }
    }

    public static class Provider implements EnvironmentProvider {

        @Override
        public Class<?> getEnvironment(ItemStack itemStack) {
            if(!itemStack.isEmpty() && itemStack.is(ModItems.ETRIONIC_BLAST_FURNACE.get())) {
                return Environment.class;
            } else {
                return null;
            }
        }
    }
}
