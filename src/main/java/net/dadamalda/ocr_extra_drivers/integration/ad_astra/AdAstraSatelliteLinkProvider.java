package net.dadamalda.ocr_extra_drivers.integration.ad_astra;

import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.common.planets.AdAstraData;
import net.dadamalda.ocr_extra_drivers.core.SatelliteLinkProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdAstraSatelliteLinkProvider extends SatelliteLinkProvider {

    @Override
    public List<ResourceLocation> getDimensionsAbove(Level level) {
        Planet planet = PlanetApi.API.getPlanet(level);
        if(planet == null) return List.of();
        Optional<ResourceKey<Level>> orbit = planet.orbit();
        return orbit.map(levelResourceKey -> List.of(levelResourceKey.location())).orElseGet(List::of);
    }

    @Override
    public List<ResourceLocation> getDimensionsBelow(Level level) {
        ResourceLocation dimension = level.dimension().location();
        for(Map.Entry<ResourceKey<Level>, Planet> planet : AdAstraData.planets().entrySet()) {
            Optional<ResourceKey<Level>> orbit = planet.getValue().orbit();
            if(orbit.isPresent() && orbit.get().location().equals(dimension)) {
                return List.of(planet.getKey().location());
            }
        }
        return List.of();
    }
}
