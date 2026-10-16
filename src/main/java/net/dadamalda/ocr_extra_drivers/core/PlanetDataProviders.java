package net.dadamalda.ocr_extra_drivers.core;

import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlanetDataProviders {
    private static List<PlanetDataProvider> PROVIDERS = new ArrayList<>();

    public static void register(PlanetDataProvider provider) {
        PROVIDERS.addFirst(provider);
    }

    public static PlanetData gather(Level level) {
        for(PlanetDataProvider provider : PROVIDERS) {
            Optional<PlanetData> data = provider.getPlanetData(level);
            if(data.isEmpty()) continue;
            return data.get();
        }
        return new PlanetData(1.0f, true, 1.0f, 15.0f);
    }
}
