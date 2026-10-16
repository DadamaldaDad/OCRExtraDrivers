package net.dadamalda.ocr_extra_drivers.core;

import net.minecraft.world.level.Level;

import java.util.Optional;

@FunctionalInterface
public interface PlanetDataProvider {
    Optional<PlanetData> getPlanetData(Level level);
}
