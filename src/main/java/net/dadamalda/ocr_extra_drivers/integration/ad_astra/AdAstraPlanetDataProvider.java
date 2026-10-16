package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.common.constants.PlanetConstants;
import net.dadamalda.ocr_extra_drivers.core.PlanetData;
import net.dadamalda.ocr_extra_drivers.core.PlanetDataProvider;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class AdAstraPlanetDataProvider implements PlanetDataProvider {
    @Override
    public Optional<PlanetData> getPlanetData(Level level) {
        Planet planet = PlanetApi.API.getPlanet(level);
        if (planet == null) return Optional.empty();
        return Optional.of(new PlanetData(
                planet.gravity()/PlanetConstants.EARTH_GRAVITY,
                planet.oxygen(),
                (float)planet.solarPower()/ PlanetConstants.EARTH_SOLAR_POWER,
                planet.temperature()
        ));
    }
}
