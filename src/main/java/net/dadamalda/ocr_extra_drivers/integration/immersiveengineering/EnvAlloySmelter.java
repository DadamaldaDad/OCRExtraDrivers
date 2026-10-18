package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AlloySmelterLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.world.inventory.ContainerData;

public class EnvAlloySmelter extends ManagedIEMultiblockEnvironment<AlloySmelterLogic.State> implements NamedBlock {

    @Override
    public String preferredName() {
        return "alloy_kiln";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvAlloySmelter(AlloySmelterLogic.State state) {
        super(state, "alloy_kiln");
    }

    @Callback(doc = "function():number -- The number of ticks that the current item has been cooking for.")
    public Object[] getCookTime(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.CURRENT_PROCESS)};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item needs to cook.")
    public Object[] getTotalCookTime(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.PROCESS_MAX)};
    }

    @Callback(doc = "function():number -- The number of ticks that the kiln will keep burning from the last consumed fuel.")
    public Object[] getBurnTime(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.BURN_TIME)};
    }

    @Callback(doc = "function():number -- The number of ticks that the currently burning fuel lasts in total.")
    public Object[] getCurrentItemBurnTime(Context context, Arguments args) {
        ContainerData stateView = state.getStateView();
        if(stateView.get(FurnaceHandler.StateView.BURN_TIME) > 0) {
            return new Object[]{stateView.get(FurnaceHandler.StateView.LAST_BURN_TIME)};
        } else {
            return new Object[]{0};
        }
    }

    @Callback(doc = "function():boolean -- Get whether the kiln is currently active.")
    public Object[] isBurning(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.BURN_TIME) > 0};
    }
}
