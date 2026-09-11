package com.truespace.datagen;

import com.truespace.TrueSpaceMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Wires every data generator to the {@code runData} Gradle task.
 * Generated files land in {@code src/generated/resources} and are committed.
 *
 * <p>Run with: {@code ./gradlew runData}
 *
 * <p>NeoForge 21.1 API notes: the {@code @EventBusSubscriber} annotation no longer
 * takes a {@code bus} attribute — the bus is detected from the event type
 * (events implementing {@code IModBusEvent} go to the mod bus, the rest to the
 * game bus). {@code GatherDataEvent} is split into {@code .Client} (assets) and
 * {@code .Server} (data) subclasses, and providers are registered via
 * {@code addProvider}/{@code createProvider} (there is no {@code ExistingFileHelper}
 * anymore).
 */
@EventBusSubscriber(modid = TrueSpaceMod.MODID)
public final class DataGenerators {

    private DataGenerators() {
    }

    /** Client assets: language files. */
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.addProvider(new ModLanguageProvider(event.getGenerator().getPackOutput(), "en_us"));
        event.addProvider(new ModLanguageProvider(event.getGenerator().getPackOutput(), "ru_ru"));
    }

    /** Server data: recipes (and, later, tags, loot tables, etc.). */
    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        event.addProvider(new ModRecipeProvider.Runner(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }
}
