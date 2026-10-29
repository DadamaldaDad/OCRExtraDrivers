package net.dadamalda.ocr_extra_drivers.mixin.ocr_extra_drivers;

import net.dadamalda.ocr_extra_drivers.init.OEDItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {
        "li.cil.oc.common.menu.Relay$$anon$1",
        "li.cil.oc.common.menu.Relay$1"
}, remap = false)
public class RelayMenuMixin {
    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void mayPlace(final ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(!stack.is(OEDItems.SATELLITE_CARD)) return;
        cir.setReturnValue(true);
    }
}
