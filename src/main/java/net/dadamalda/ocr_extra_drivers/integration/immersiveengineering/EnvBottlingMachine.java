package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.bottling_machine.BottlingMachineLogic;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.world.level.Level;

public class EnvBottlingMachine extends ManagedIEMultiblockEnvironment<BottlingMachineLogic.State> implements NamedBlock {

    private final Level level;

    @Override
    public String preferredName() {
        return "bottling_machine";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvBottlingMachine(BottlingMachineLogic.State state, Level level) {
        super(state, "bottling_machine");
        this.level = level;
    }

    @Callback(doc = "function():number -- Returns the amount of energy stored in the machine.")
    public Object[] getEnergyStored(Context context, Arguments args) {
        return new Object[]{state.getEnergy().getEnergyStored()};
    }

    @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the machine.")
    public Object[] getMaxEnergyStored(Context context, Arguments args) {
        return new Object[]{state.getEnergy().getMaxEnergyStored()};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item has been filling for.")
    public Object[] getFillTime(Context context, Arguments args) {
        if(!state.processor.getQueue().isEmpty()) {
            return new Object[]{state.processor.getQueue().getFirst().processTick};
        }
        return new Object[]{0};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item needs to fill.")
    public Object[] getTotalFillTime(Context context, Arguments args) {
        if(!state.processor.getQueue().isEmpty()) {
            return new Object[]{state.processor.getQueue().getFirst().getMaxTicks(level)};
        }
        return new Object[]{0};
    }
}
