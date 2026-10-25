package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.dadamalda.ocr_extra_drivers.core.PlanetData;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class StellarisPlanetDataProvider implements PlanetDataProvider {
    @Override
    public Optional<PlanetData> getPlanetData(Level level) {
        ResourceLocation dimension = level.dimension().location();
        Planet planet = PlanetUtil.getPlanet(dimension);
        return Optional.of(new PlanetData(
                planet.gravity()/9.80665f,
                planet.oxygen(),
                1.0f,
                planet.temperature()
        ));
    }
}
