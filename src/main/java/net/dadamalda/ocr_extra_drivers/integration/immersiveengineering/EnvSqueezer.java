package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.SqueezerLogic;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import net.dadamalda.ocr_extra_drivers.duck.SqueezerStateMixinDuck;
import net.minecraft.world.level.Level;

public class EnvSqueezer extends ManagedIEMultiblockEnvironment<SqueezerLogic.State> implements NamedBlock {

    private final Level level;

    @Override
    public String preferredName() {
        return "squeezer";
    }

    @Override
    public int priority() {
        return 0;
    }

    public EnvSqueezer(SqueezerLogic.State state, Level level) {
        super(state, "squeezer");
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

    @Callback(doc = "function():number -- The number of ticks that the current item has been squeezing for.")
    public Object[] getSqueezeTime(Context context, Arguments args) {
        var processor = ((SqueezerStateMixinDuck)state).OCRExtraDrivers$getProcessor();
        if(!processor.getQueue().isEmpty()) {
            return new Object[]{processor.getQueue().getFirst().processTick};
        }
        return new Object[]{0};
    }

    @Callback(doc = "function():number -- The number of ticks that the current item needs to squeeze.")
    public Object[] getTotalSqueezeTime(Context context, Arguments args) {
        var processor = ((SqueezerStateMixinDuck)state).OCRExtraDrivers$getProcessor();
        if(!processor.getQueue().isEmpty()) {
            return new Object[]{processor.getQueue().getFirst().getMaxTicks(level)};
        }
        return new Object[]{0};
    }
}
