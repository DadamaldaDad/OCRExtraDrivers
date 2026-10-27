package net.dadamalda.ocr_extra_drivers.init;

import li.cil.oc.common.item.BasicTieredComponentItem;
import net.dadamalda.ocr_extra_drivers.OCRExtraDrivers;
import net.dadamalda.ocr_extra_drivers.item.WorldSensorCard;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class OEDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(OCRExtraDrivers.MODID);

    public static final DeferredHolder<Item, BasicTieredComponentItem> WORLD_SENSOR_CARD = ITEMS.register("worldsensorcard", () -> new BasicTieredComponentItem(new Item.Properties(), "worldsensorcard"));
    public static final DeferredHolder<Item, BasicTieredComponentItem> SATELLITE_CARD = ITEMS.register("satellitecard", () -> new BasicTieredComponentItem(new Item.Properties(), "satellitecard"));
}
