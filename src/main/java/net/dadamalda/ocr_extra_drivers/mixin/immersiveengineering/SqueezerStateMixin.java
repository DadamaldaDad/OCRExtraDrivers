package net.dadamalda.ocr_extra_drivers.mixin.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.SqueezerLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import net.dadamalda.ocr_extra_drivers.duck.SqueezerStateMixinDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SqueezerLogic.State.class)
public class SqueezerStateMixin implements SqueezerStateMixinDuck {
    @Shadow
    @Final
    private MultiblockProcessor.InMachineProcessor<SqueezerRecipe> processor;

    @Override
    public MultiblockProcessor.InMachineProcessor<SqueezerRecipe> OCRExtraDrivers$getProcessor() {
        return processor;
    }
}
