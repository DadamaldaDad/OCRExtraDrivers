package net.dadamalda.ocr_extra_drivers.init;

import net.dadamalda.ocr_extra_drivers.OCRExtraDrivers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class OEDTags {
    public static final TagKey<Block> TRANSPARENT_TO_SATELLITE = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(OCRExtraDrivers.MODID, "transparent_to_satellite"));
}
