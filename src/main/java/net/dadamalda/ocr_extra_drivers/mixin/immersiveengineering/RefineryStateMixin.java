package net.dadamalda.ocr_extra_drivers.mixin.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.RefineryLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import net.dadamalda.ocr_extra_drivers.duck.RefineryStateMixinDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RefineryLogic.State.class)
public class RefineryStateMixin implements RefineryStateMixinDuck {
    @Shadow
    @Final
    private MultiblockProcessor.InMachineProcessor<RefineryRecipe> processor;

    @Override
    public MultiblockProcessor.InMachineProcessor<RefineryRecipe> OCRExtraDrivers$getProcessor() {
        return processor;
    }
}
