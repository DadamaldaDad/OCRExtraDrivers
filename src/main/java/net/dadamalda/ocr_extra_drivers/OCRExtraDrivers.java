package net.dadamalda.ocr_extra_drivers;

import li.cil.oc.api.Driver;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;
import net.dadamalda.ocr_extra_drivers.init.OEDCreativeModeTabs;
import net.dadamalda.ocr_extra_drivers.init.OEDItems;
import net.dadamalda.ocr_extra_drivers.integration.ad_astra.AdAstraPlanetDataProvider;
import net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers.DriverWorldSensorCard;
import net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers.HardcodedPlanetDataProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(OCRExtraDrivers.MODID)
public class OCRExtraDrivers {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "ocr_extra_drivers";

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public OCRExtraDrivers(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        OEDItems.ITEMS.register(modEventBus);
        OEDCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Driver.add(DriverWorldSensorCard.INSTANCE);
        Driver.add(DriverWorldSensorCard.Provider.INSTANCE);

        if(ModList.get().isLoaded("ad_astra"))
            PlanetDataProviders.register(new AdAstraPlanetDataProvider());

        PlanetDataProviders.register(new HardcodedPlanetDataProvider());
    }
}
