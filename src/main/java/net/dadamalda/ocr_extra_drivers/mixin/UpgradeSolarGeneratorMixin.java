package net.dadamalda.ocr_extra_drivers.mixin;

import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.server.component.UpgradeSolarGenerator;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UpgradeSolarGenerator.class)
public class UpgradeSolarGeneratorMixin {
    @Shadow
    @Final
    private EnvironmentHost host;
    @Unique
    private float OCRExtraDrivers$solarEfficiency = 1.0f;

    @Inject(method = "isSunVisible", at = @At("RETURN"))
    private void calculateSolarEfficiency(CallbackInfoReturnable<Boolean> cir) {
        OCRExtraDrivers$solarEfficiency = PlanetDataProviders.gather(host.getEnvironmentLevel()).solarPower();
    }

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lli/cil/oc/api/network/Connector;changeBuffer(D)D"))
    private double updateChangeBuffer(double v) {
        return v * OCRExtraDrivers$solarEfficiency;
    }
}
