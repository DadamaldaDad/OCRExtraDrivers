package net.dadamalda.ocr_extra_drivers.integration.immersiveengineering;

import li.cil.oc.api.Driver;

public class ModImmersiveEngineering {
    public static void initialize() {
        Driver.add(new DriverMultiblockBlockEntityMaster());
        Driver.add(new DriverMultiblockBlockEntityDummy());
        Driver.add(new IEMultiblockDrivers.Provider());

        Driver.add(new DriverAccumulator());
        Driver.add(new DriverPowerMeter());
        Driver.add(new DriverCloche());

        Driver.add(new DriverAccumulator.Provider());
        Driver.add(new DriverPowerMeter.Provider());
        Driver.add(new DriverCloche.Provider());
    }
}
