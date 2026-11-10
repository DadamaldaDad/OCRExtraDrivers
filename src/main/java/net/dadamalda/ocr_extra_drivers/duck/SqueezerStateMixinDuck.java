package net.dadamalda.ocr_extra_drivers.duck;

import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;

public interface SqueezerStateMixinDuck {
    MultiblockProcessor.InMachineProcessor<SqueezerRecipe> OCRExtraDrivers$getProcessor();
}
