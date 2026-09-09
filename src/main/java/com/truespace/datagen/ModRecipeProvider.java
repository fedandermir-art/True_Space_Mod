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
 * <p>No recipes are generated yet: the bauxite → aluminium chain is driven by
 * machines (crusher, Bayer digester, Hall-Héroult cell) that arrive with their
 * own recipe types in the next milestone. Any crafting-table recipe added here
 * from now on must be a real assembly step.
 */
public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // TODO(milestone): machine recipes (crusher, Bayer, Hall-Héroult) —
        // they need custom recipe types, which land together with the machines.
    }
}
