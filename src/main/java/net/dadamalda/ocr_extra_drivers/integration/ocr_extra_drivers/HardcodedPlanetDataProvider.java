package net.dadamalda.ocr_extra_drivers.integration.ocr_extra_drivers;

import net.dadamalda.ocr_extra_drivers.core.PlanetData;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProvider;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class HardcodedPlanetDataProvider implements PlanetDataProvider {

    @Override
    public Optional<PlanetData> getPlanetData(Level level) {
        return switch (level.dimension().location().toString()) {
            case "minecraft:overworld" -> Optional.of(new PlanetData(1.0f, true, 1.0f, 15.0f));
            case "minecraft:the_nether" -> Optional.of(new PlanetData(1.0f, true, 0.0f, 50.0f));
            case "minecraft:the_end" -> Optional.of(new PlanetData(1.0f, true, 1.0f, 5.0f));
            default -> Optional.empty();
        };
    }
}
