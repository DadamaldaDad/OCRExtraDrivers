package net.dadamalda.ocr_extra_drivers.integration.stellaris;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.dadamalda.ocr_extra_drivers.core.SatelliteLinkProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;

public class StellarisSatelliteLinkProvider extends SatelliteLinkProvider {
    @Override
    public List<ResourceLocation> getDimensionsAbove(Level level) {
        Planet planet = PlanetUtil.getPlanet(level.dimension().location());
        if(planet == null) return List.of();
        if(planet.orbit().isEmpty()) return List.of();
        return List.of(planet.orbit().get());
    }

    @Override
    public List<ResourceLocation> getDimensionsBelow(Level level) {
        Planet planet = PlanetUtil.getPlanetFromOrbit(level.dimension().location());
        if(planet == null) return List.of();
        return List.of(planet.dimension());
    }
}
