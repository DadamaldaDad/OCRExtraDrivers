package net.dadamalda.ocr_extra_drivers.mixin.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FermenterLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import net.dadamalda.ocr_extra_drivers.duck.ClocheBlockEntityMixinDuck;
import net.dadamalda.ocr_extra_drivers.duck.FermenterStateMixinDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FermenterLogic.State.class)
public class FermenterStateMixin implements FermenterStateMixinDuck {
    @Shadow
    @Final
    private MultiblockProcessor<FermenterRecipe, ProcessContext.ProcessContextInMachine<FermenterRecipe>> processor;

    @Override
    public MultiblockProcessor<FermenterRecipe, ProcessContext.ProcessContextInMachine<FermenterRecipe>> OCRExtraDrivers$getProcessor() {
        return processor;
    }
}
