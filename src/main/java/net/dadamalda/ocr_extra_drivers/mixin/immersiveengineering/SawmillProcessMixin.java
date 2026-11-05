package net.dadamalda.ocr_extra_drivers.mixin.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.sawmill.SawmillProcess;
import net.dadamalda.ocr_extra_drivers.duck.SawmillProcessMixinDuck;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SawmillProcess.class)
public abstract class SawmillProcessMixin implements SawmillProcessMixinDuck {
    @Shadow
    private int processTick;

    @Shadow
    public abstract float getRelativeProcessStep(Level level);

    @Override
    public float OCRExtraDrivers$getMaxProcessTicks(Level level) {
        return processTick / getRelativeProcessStep(level);
    }

    @Override
    public float OCRExtraDrivers$getProcessTicks() {
        return processTick;
    }
}
