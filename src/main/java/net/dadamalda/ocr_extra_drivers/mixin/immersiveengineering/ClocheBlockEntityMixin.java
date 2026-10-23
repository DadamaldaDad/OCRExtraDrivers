package net.dadamalda.ocr_extra_drivers.mixin.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import net.dadamalda.ocr_extra_drivers.duck.ClocheBlockEntityMixinDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClocheBlockEntity.class)
public class ClocheBlockEntityMixin implements ClocheBlockEntityMixinDuck {

    @Shadow
    private float growth;

    @Override
    public float OCRExtraDrivers$getGrowTime() {
        return growth;
    }
}
