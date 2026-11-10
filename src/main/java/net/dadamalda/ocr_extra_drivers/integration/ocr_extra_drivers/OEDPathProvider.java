package net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers;

import li.cil.oc.api.manual.PathProvider;
import net.dadamalda.ocr_extra_drivers.OCRExtraDrivers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OEDPathProvider implements PathProvider {
    @Override
    public String pathFor(ItemStack itemStack) {
        ResourceLocation location = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        if(!location.getNamespace().equals(OCRExtraDrivers.MODID)) return null;
        if(itemStack.getItem() instanceof BlockItem)
            return "%LANGUAGE%/ocr_extra_drivers/block/"+location.getPath()+".md";
        return "%LANGUAGE%/ocr_extra_drivers/item/"+location.getPath()+".md";
    }

    @Override
    public String pathFor(Level level, BlockPos blockPos) {
        return null;
    }
}
