package com.truespace;

import com.truespace.init.ModBlocks;
import com.truespace.init.ModCreativeTabs;
import com.truespace.init.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

/**
 * True Space Mod — large, realistic hardcore space-exploration mod.
 *
 * <p>The value of {@link #MODID} must match the {@code mod_id} property in
 * {@code gradle.properties} and the {@code modId} field in
 * {@code META-INF/neoforge.mods.toml}.
 *
 * <p>Architecture notes (see {@code docs/}):
 * <ul>
 *   <li>Registries live in {@code com.truespace.init} (items, blocks, fluids, tabs...).</li>
 *   <li>Datagen lives in {@code com.truespace.datagen} and writes to {@code src/generated/resources}.</li>
 *   <li>The design-time content registry lives in {@code content/} (JSON, not loaded by the game).</li>
 * </ul>
 */
@Mod(TrueSpaceMod.MODID)
public class TrueSpaceMod {

    /** Unique mod id. Must be lowercase, matching gradle.properties / neoforge.mods.toml. */
    public static final String MODID = "truespace";

    /** Mod logger (SLF4J). */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Mod constructor: first code executed when the mod loads.
     * FML injects {@link IEventBus} and {@link ModContainer} automatically.
     */
    public TrueSpaceMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register our DeferredRegisters on the mod event bus.
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);

        // Register the mod config (common = synced from server, plain file on server).
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        LOGGER.info("True Space Mod loaded. Realism enabled, good luck in the void.");
    }
}
