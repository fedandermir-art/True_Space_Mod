package com.truespace.datagen;

import com.truespace.init.ModItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

/**
 * Generates {@code data/truespace/recipe/*.json}.
 *
 * <p><b>Project rule:</b> final recipes must reflect the real process.
 * E.g. aluminium comes from alumina by Hall-Héroult electrolysis inside a
 * machine, <i>not</i> from a crafting table. Crafting-table recipes are only
 * for real assembly steps (parts into components).
 */
public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // ------------------------------------------------------------------
        // TEMPORARY placeholder recipe — proves the datagen -> recipe pipeline.
        // Will be REPLACED by the aluminium production chain in milestone 2
        // (bauxite -> alumina via Bayer process -> aluminium via electrolysis).
        // No unlock criterion on purpose, so the committed generated output
        // matches `runData` exactly (no auto-generated advancement file).
        // ------------------------------------------------------------------
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ALUMINUM_INGOT.get(), 1)
                .pattern("II")
                .pattern("II")
                .define('I', Items.IRON_INGOT)
                .save(output);
    }
}
