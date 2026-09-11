package com.truespace;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Mod configuration.
 *
 * <p>Early master toggles for the realism/hardcore systems. Individual systems
 * will get their own dedicated config sections as they are implemented; these
 * top-level switches let us (and players) turn whole pillars on/off while the
 * mod is in development.
 */
public final class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ------------------------------------------------------------------
    // Realism pillars
    // ------------------------------------------------------------------

    /** Realistic material chains: beneficiation, electrolysis, by-products, real yields. */
    public static final ModConfigSpec.BooleanValue REALISTIC_MATERIALS = BUILDER
            .comment("Enable realistic ore->metal processing chains (beneficiation, electrolysis, by-products).")
            .define("realisticMaterials", true);

    /** Life support: oxygen partial pressure, CO2 scrubbing, water, temperature. */
    public static final ModConfigSpec.BooleanValue LIFE_SUPPORT = BUILDER
            .comment("Enable the life-support system (oxygen, CO2, water, pressure).")
            .define("lifeSupport", true);

    /** Ionizing radiation: sources, dose (Sv), shielding, radiation sickness. */
    public static final ModConfigSpec.BooleanValue RADIATION = BUILDER
            .comment("Enable the ionizing-radiation system (dose in Sv, shielding, sickness).")
            .define("radiation", true);

    /** Real rocket physics: delta-v, specific impulse, Tsiolkovsky equation, staging. */
    public static final ModConfigSpec.BooleanValue REALISTIC_ROCKETRY = BUILDER
            .comment("Enable realistic rocketry (delta-v budget, Isp, Tsiolkovsky equation).")
            .define("realisticRocketry", true);

    /** Show real physical data (density, melting point, Isp...) in item tooltips. */
    public static final ModConfigSpec.BooleanValue REAL_DATA_TOOLTIPS = BUILDER
            .comment("Show real-world physical data in tooltips (density, melting point, Isp...).")
            .define("realDataTooltips", true);

    // ------------------------------------------------------------------

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }
}
