package net.dadamalda.ocr_extra_drivers.duck;

import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;

public interface FermenterStateMixinDuck {
    MultiblockProcessor<FermenterRecipe, ProcessContext.ProcessContextInMachine<FermenterRecipe>> OCRExtraDrivers$getProcessor();
}
