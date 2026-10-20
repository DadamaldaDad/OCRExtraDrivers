package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;

public class EnvCokeOven extends ManagedIEMultiblockEnvironment<CokeOvenLogic.State> implements NamedBlock {

    @Override
    public String preferredName() {
        return "coke_oven";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvCokeOven(CokeOvenLogic.State state) {
        super(state, "coke_oven");
    }

    @Callback(doc = "function():number -- The number of ticks that the coke oven will keep burning from the last consumed fuel.")
    public Object[] getBurnTime(Context context, Arguments args) {
        return new Object[]{state.get(CokeOvenLogic.State.BURN_TIME)};
    }

    @Callback(doc = "function():number -- The number of ticks that the currently burning fuel lasts in total.")
    public Object[] getCurrentItemBurnTime(Context context, Arguments args) {
        if(state.get(CokeOvenLogic.State.BURN_TIME) > 0) {
            return new Object[]{state.get(CokeOvenLogic.State.MAX_BURN_TIME)};
        } else {
            return new Object[]{0};
        }
    }
}
