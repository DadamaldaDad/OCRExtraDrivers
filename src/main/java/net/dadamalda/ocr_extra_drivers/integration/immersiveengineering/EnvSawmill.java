package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.sawmill.SawmillLogic;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.dadamalda.ocr_extra_drivers.duck.SawmillProcessMixinDuck;
import net.minecraft.world.level.Level;

public class EnvSawmill extends ManagedIEMultiblockEnvironment<SawmillLogic.State> implements NamedBlock {

    private final Level level;

    @Override
    public String preferredName() {
        return "sawmill";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvSawmill(SawmillLogic.State state, Level level) {
        super(state, "sawmill");
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

    @Callback(doc = "function():number -- The number of ticks that the current item has been cutting for.")
    public Object[] getCutTime(Context context, Arguments args) {
        if(!state.sawmillProcessQueue.isEmpty()) {
            return new Object[]{((SawmillProcessMixinDuck)state.sawmillProcessQueue.getFirst()).OCRExtraDrivers$getProcessTicks()};
        }
        return new Object[]{0};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item needs to get cut.")
    public Object[] getTotalCutTime(Context context, Arguments args) {
        if(!state.sawmillProcessQueue.isEmpty()) {
            return new Object[]{((SawmillProcessMixinDuck)state.sawmillProcessQueue.getFirst()).OCRExtraDrivers$getMaxProcessTicks(level)};
        }
        return new Object[]{0};
    }
}
