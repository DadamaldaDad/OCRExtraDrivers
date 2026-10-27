package net.dadamalda.ocr_extra_drivers.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class SatelliteLinkProvider {
    public abstract List<ResourceLocation> getDimensionsAbove(Level level);
    public abstract List<ResourceLocation> getDimensionsBelow(Level level);
}
