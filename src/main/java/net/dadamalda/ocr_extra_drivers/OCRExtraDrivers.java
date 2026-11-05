package net.dadamalda.ocr_extra_drivers;

import li.cil.oc.api.Driver;
import li.cil.oc.api.Manual;
import li.cil.oc.api.prefab.ItemStackTabIconRenderer;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProviders;
import net.dadamalda.ocr_extra_drivers.init.OEDCreativeModeTabs;
import net.dadamalda.ocr_extra_drivers.init.OEDItems;
import net.dadamalda.ocr_extra_drivers.integration.ad_astra.ModAdAstra;
import net.dadamalda.ocr_extra_drivers.integration.cb_multipart.ModCBMultipart;
import net.dadamalda.ocr_extra_drivers.integration.immersiveengineering.ModImmersiveEngineering;
import net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers.DriverSatelliteCard;
import net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers.DriverWorldSensorCard;
import net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers.HardcodedPlanetDataProvider;
import net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers.OEDPathProvider;
import net.dadamalda.ocr_extra_drivers.integration.stellaris.ModStellaris;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
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
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(Config::load);

        OEDItems.ITEMS.register(modEventBus);
        OEDCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        Driver.add(DriverWorldSensorCard.INSTANCE);
        Driver.add(DriverWorldSensorCard.Provider.INSTANCE);
        Driver.add(DriverSatelliteCard.INSTANCE);
        Driver.add(DriverSatelliteCard.Provider.INSTANCE);

        if(ModList.get().isLoaded("stellaris"))
            ModStellaris.initialize();
        if(ModList.get().isLoaded("ad_astra"))
            ModAdAstra.initialize();
        if(ModList.get().isLoaded("immersiveengineering"))
            ModImmersiveEngineering.initialize();
        if(ModList.get().isLoaded("cb_multipart"))
            ModCBMultipart.initialize();

        PlanetDataProviders.register(new HardcodedPlanetDataProvider());
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        Manual.addProvider(new OEDPathProvider());
        Manual.addTab(
                new ItemStackTabIconRenderer(new ItemStack(OEDItems.SATELLITE_CARD)),
                "gui.ocr_extra_drivers.manual_tab",
                "%LANGUAGE%/ocr_extra_drivers/index.md"
        );
    }
}
