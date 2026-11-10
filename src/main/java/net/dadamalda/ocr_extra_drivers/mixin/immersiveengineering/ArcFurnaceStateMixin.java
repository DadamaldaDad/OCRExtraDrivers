package net.dadamalda.ocr_extra_drivers.mixin.immersiveengineering;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessor;
import net.dadamalda.ocr_extra_drivers.duck.ArcFurnaceStateMixinDuck;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArcFurnaceLogic.State.class)
public class ArcFurnaceStateMixin implements ArcFurnaceStateMixinDuck {
    @Shadow
    @Final
    private MultiblockProcessor.InMachineProcessor<ArcFurnaceRecipe> processor;

    @Override
    public MultiblockProcessor.InMachineProcessor<ArcFurnaceRecipe> OCRExtraDrivers$getProcessor() {
        return processor;
    }
}
