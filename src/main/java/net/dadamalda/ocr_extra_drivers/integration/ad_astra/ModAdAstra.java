package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import li.cil.oc.api.Driver;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;

public class ModAdAstra {
    public static void initialize() {
        Driver.add(new DriverCoalGenerator());
        Driver.add(new DriverCompressor());
        Driver.add(new DriverEtrionicBlastFurnace());
        Driver.add(new DriverFuelRefinery());
        Driver.add(new DriverOxygenLoader());
        Driver.add(new DriverSolarPanel());
        Driver.add(new DriverWaterPump());
        Driver.add(new DriverOxygenDistributor());
        Driver.add(new DriverGravityNormalizer());
        Driver.add(new DriverEnergizer());
        Driver.add(new DriverCryoFreezer());

        Driver.add(new DriverCoalGenerator.Provider());
        Driver.add(new DriverCompressor.Provider());
        Driver.add(new DriverEtrionicBlastFurnace.Provider());
        Driver.add(new DriverFuelRefinery.Provider());
        Driver.add(new DriverOxygenLoader.Provider());
        Driver.add(new DriverSolarPanel.Provider());
        Driver.add(new DriverWaterPump.Provider());
        Driver.add(new DriverOxygenDistributor.Provider());
        Driver.add(new DriverGravityNormalizer.Provider());
        Driver.add(new DriverEnergizer.Provider());
        Driver.add(new DriverCryoFreezer.Provider());

        PlanetDataProviders.register(new AdAstraPlanetDataProvider());
    }
}
