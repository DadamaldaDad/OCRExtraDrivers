package net.dadamalda.ocr_extra_drivers.duck;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;

public interface ArcFurnaceStateMixinDuck {
    MultiblockProcessor.InMachineProcessor<ArcFurnaceRecipe> OCRExtraDrivers$getProcessor();
}
