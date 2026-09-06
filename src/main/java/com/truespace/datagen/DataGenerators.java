package com.truespace.datagen;

import com.truespace.TrueSpaceMod;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Wires every data generator to the {@code runData} Gradle task.
 * Generated files land in {@code src/generated/resources} and are committed,
 * following the MDK convention (see {@code .gitattributes}).
 *
 * <p>Run with: {@code ./gradlew runData}
 */
@EventBusSubscriber(modid = TrueSpaceMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators {

    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Client-side assets.
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(output, "ru_ru"));

        // Server-side data.
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, lookupProvider));

        // TODO(milestone 2+): block/item tags, loot tables, blockstates, models.
    }
}
