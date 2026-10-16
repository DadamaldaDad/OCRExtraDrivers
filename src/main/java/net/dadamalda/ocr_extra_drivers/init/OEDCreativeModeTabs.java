package net.dadamalda.ocr_extra_drivers.init;

import net.dadamalda.ocr_extra_drivers.OCRExtraDrivers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class OEDCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OCRExtraDrivers.MODID);

    public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ocr_extra_drivers"))
                    .icon(() -> new ItemStack(OEDItems.WORLD_SENSOR_CARD))
                    .displayItems((params, output) -> {
                        output.accept(OEDItems.WORLD_SENSOR_CARD.get());
                    })
                    .build());
}
