package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;

public class ModStellaris {
    public static void initialize() {
        PlanetDataProviders.register(new StellarisPlanetDataProvider());
    }
}
