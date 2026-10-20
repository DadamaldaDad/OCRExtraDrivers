package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AdvBlastFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

public class EnvAdvBlastFurnace extends ManagedIEMultiblockEnvironment<AdvBlastFurnaceLogic.State> implements NamedBlock {

    private final IMultiblockLevel level;

    @Override
    public String preferredName() {
        return "improved_blast_furnace";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvAdvBlastFurnace(AdvBlastFurnaceLogic.State state, IMultiblockLevel level) {
        super(state, "improved_blast_furnace");
        this.level = level;
    }

    @Callback(doc = "function():number -- The number of ticks that the current item has been cooking for.")
    public Object[] getCookTime(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.CURRENT_PROCESS)};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item needs to cook.")
    public Object[] getTotalCookTime(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.PROCESS_MAX)};
    }

    @Callback(doc = "function():number -- The number of ticks that the blast furnace will keep burning from the last consumed fuel.")
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

    @Callback(doc = "function():boolean -- Get whether the blast furnace is currently active.")
    public Object[] isBurning(Context context, Arguments args) {
        return new Object[]{state.getStateView().get(FurnaceHandler.StateView.BURN_TIME) > 0};
    }

    @Callback(doc = "function():number -- The number of active preheaters connected to the blast furnace.")
    public Object[] getActivePreheaterCount(Context context, Arguments args) {
        int result = 0;
        for(int i = 0; i < 2; i++) {
            if(state.preheaterActive(level, i).get()) {
                result++;
            }
        }
        return new Object[]{result};
    }

    @Callback(doc = "function():number -- The total number of preheaters connected to the blast furnace.")
    public Object[] getTotalPreheaterCount(Context context, Arguments args) {
        int result = 0;
        if(state.getPreheater(level, new BlockPos(-1, 0, 1)) != null) {
            result++;
        }
        if(state.getPreheater(level, new BlockPos(3, 0, 1)) != null) {
            result++;
        }
        return new Object[]{result};
    }
}
