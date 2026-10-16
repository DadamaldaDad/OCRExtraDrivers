package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import li.cil.oc.api.Driver;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;

public class ModAdAstra {
    public static void initialize() {
        Driver.add(new DriverEtrionicBlastFurnace());

        Driver.add(new DriverEtrionicBlastFurnace.Provider());

        PlanetDataProviders.register(new AdAstraPlanetDataProvider());
    }
}
