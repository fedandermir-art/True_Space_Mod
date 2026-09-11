package com.truespace.datagen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

/**
 * Generates {@code data/truespace/recipe/*.json}.
 *
 * <p><b>Project rule:</b> final recipes must reflect the real process.
 * E.g. aluminium comes from alumina by Hall-Héroult electrolysis inside a
 * machine, <i>not</i> from a crafting table. Crafting-table recipes are only
 * for real assembly steps (parts into components).
 *
 * <p>NeoForge 21.1 API: {@code RecipeProvider} is no longer a {@code DataProvider}.
 * It is constructed with {@code (HolderLookup.Provider, RecipeOutput)} and its
 * {@code buildRecipes()} takes no arguments (the output is a protected field).
 * The inner {@code Runner} is the actual {@code DataProvider} registered with
 * {@code GatherDataEvent}.
 */
public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // TODO(milestone): machine recipes (crusher, Bayer, Hall-Héroult) —
        // they need custom recipe types, which land together with the machines.
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "True Space Recipes";
        }
    }
}
