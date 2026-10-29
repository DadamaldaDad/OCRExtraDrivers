package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import li.cil.oc.api.Driver;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;
import net.dadamalda.ocr_extra_drivers.core.SatelliteNetwork;

public class ModStellaris {
    public static void initialize() {
        Driver.add(new DriverOxygenDistributor());
        Driver.add(new DriverSolarPanel());
        Driver.add(new DriverCoalGenerator());
        Driver.add(new DriverRadioactiveGenerator());
        Driver.add(new DriverDieselGenerator());
        Driver.add(new DriverWaterSeparator());
        Driver.add(new DriverFuelRefinery());
        Driver.add(new DriverVacuumator());
        Driver.add(new DriverPowerBank());

        Driver.add(new DriverOxygenDistributor.Provider());
        Driver.add(new DriverSolarPanel.Provider());
        Driver.add(new DriverCoalGenerator.Provider());
        Driver.add(new DriverRadioactiveGenerator.Provider());
        Driver.add(new DriverDieselGenerator.Provider());
        Driver.add(new DriverWaterSeparator.Provider());
        Driver.add(new DriverFuelRefinery.Provider());
        Driver.add(new DriverVacuumator.Provider());
        Driver.add(new DriverPowerBank.Provider());

        PlanetDataProviders.register(new StellarisPlanetDataProvider());
        SatelliteNetwork.registerLinkProvider(new StellarisSatelliteLinkProvider());
    }
}
