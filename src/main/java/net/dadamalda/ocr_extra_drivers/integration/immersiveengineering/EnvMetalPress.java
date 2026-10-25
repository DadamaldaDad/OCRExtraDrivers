package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.MetalPressLogic;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.minecraft.world.level.Level;

public class EnvMetalPress extends ManagedIEMultiblockEnvironment<MetalPressLogic.State> implements NamedBlock {

    private final Level level;

    @Override
    public String preferredName() {
        return "metal_press";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvMetalPress(MetalPressLogic.State state, Level level) {
        super(state, "metal_press");
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

    @Callback(doc = "function():number -- The number of ticks that the current item has been pressing for.")
    public Object[] getPressTime(Context context, Arguments args) {
        if(!state.processor.getQueue().isEmpty()) {
            return new Object[]{state.processor.getQueue().getFirst().processTick};
        }
        return new Object[]{0};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item needs to press.")
    public Object[] getTotalPressTime(Context context, Arguments args) {
        if(!state.processor.getQueue().isEmpty()) {
            return new Object[]{state.processor.getQueue().getFirst().getMaxTicks(level)};
        }
        return new Object[]{0};
    }
}
