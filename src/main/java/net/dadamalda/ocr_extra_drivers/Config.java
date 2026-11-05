package net.dadamalda.ocr_extra_drivers;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_SOLAR_EFFICIENCY = BUILDER
            .push("space")
            .comment("If enabled, the solar generator upgrade's generation rate will be scaled based on the planet it's on")
            .define("enable_solar_efficiency", true);

    private static final ModConfigSpec.IntValue SATELLITE_DISTANCE = BUILDER
            .comment("The distance given to computers for satellite packets and used for power consumption calculation")
            .defineInRange("satellite_distance", 512, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue SATELLITE_CHECK_DELAY = BUILDER
            .comment("How often in ticks can the satellite card check that it can see the sky/ground")
            .comment("Note: No checks will be made if there is no network activity")
            .defineInRange("satellite_check_delay", 40, 0, 20*60);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enable_solar_efficiency;
    public static int satellite_distance;
    public static int satellite_check_delay;

    public static void load(ModConfigEvent event) {
        enable_solar_efficiency = ENABLE_SOLAR_EFFICIENCY.get();
        satellite_distance = SATELLITE_DISTANCE.get();
        satellite_check_delay = SATELLITE_CHECK_DELAY.get();
    }
}
