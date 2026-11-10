package net.dadamalda.ocr_extra_drivers.duck;

import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;

public interface RefineryStateMixinDuck {
    MultiblockProcessor.InMachineProcessor<RefineryRecipe> OCRExtraDrivers$getProcessor();
}
