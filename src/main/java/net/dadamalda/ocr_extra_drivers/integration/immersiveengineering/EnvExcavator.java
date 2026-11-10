package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.ExcavatorLogic;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class EnvExcavator extends ManagedIEMultiblockEnvironment<ExcavatorLogic.State> implements NamedBlock {
    @Override
    public String preferredName() {
        return "excavator";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvExcavator(ExcavatorLogic.State state) {
        super(state, "excavator");
    }

    @Callback(doc = "function():number -- Returns the amount of energy stored in the machine.")
    public Object[] getEnergyStored(Context context, Arguments args) {
        return new Object[]{state.getEnergy().getEnergyStored()};
    }

    @Callback(doc = "function():number -- Returns the maximum amount of energy stored in the machine.")
    public Object[] getMaxEnergyStored(Context context, Arguments args) {
        return new Object[]{state.getEnergy().getMaxEnergyStored()};
    }

    @Callback(doc = "function():boolean -- Returns whether the excavator is active.")
    public Object[] isActive(Context context, Arguments args) {
        return new Object[]{state.isActive()};
    }
}
