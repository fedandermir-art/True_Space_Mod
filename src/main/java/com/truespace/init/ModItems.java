package com.truespace.init;

import com.truespace.TrueSpaceMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All mod items are declared here, in one place, so the content stays
 * greppable and the design-time registry in {@code content/} can be
 * cross-checked against the code.
 *
 * <p>Rule of the project: every item added here MUST also have an entry in
 * {@code content/registry/} with real-world data and a source. Content without
 * a real basis does not ship.
 */
public final class ModItems {

    /** Central DeferredRegister for items, registered against the mod event bus. */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TrueSpaceMod.MODID);

    // ==================================================================
    // Metals
    // ==================================================================

    /**
     * The first real metal of the mod.
     * Produced by Hall-Héroult electrolysis of alumina — not by hand.
     * See {@code content/registry/materials.json}.
     */
    public static final DeferredItem<Item> ALUMINUM_INGOT = ITEMS.registerSimpleItem("aluminum_ingot");

    // ==================================================================
    // Bauxite → aluminium chain (Phase 1)
    // ==================================================================

    /** Bauxite reduced to size by the crusher — first beneficiation step. */
    public static final DeferredItem<Item> CRUSHED_BAUXITE = ITEMS.registerSimpleItem("crushed_bauxite");

    /** Refined aluminium oxide; the Hall-Héroult feedstock. */
    public static final DeferredItem<Item> ALUMINA = ITEMS.registerSimpleItem("alumina");

    /** Molten-salt electrolyte that dissolves alumina (Na3AlF6). */
    public static final DeferredItem<Item> CRYOLITE = ITEMS.registerSimpleItem("cryolite");

    /** Consumable electrode; burns to CO2 during electrolysis. */
    public static final DeferredItem<Item> CARBON_ANODE = ITEMS.registerSimpleItem("carbon_anode");

    /** Caustic by-product of the Bayer process (hazardous waste). */
    public static final DeferredItem<Item> RED_MUD = ITEMS.registerSimpleItem("red_mud");

    // ==================================================================
    // Ore block items (blocks themselves live in ModBlocks)
    // ==================================================================

    public static final DeferredItem<BlockItem> BAUXITE_ORE =
            ITEMS.registerSimpleBlockItem("bauxite_ore", ModBlocks.BAUXITE_ORE);

    public static final DeferredItem<BlockItem> CRYO_CHAMBER =
            ITEMS.registerSimpleBlockItem("cryo_chamber", ModBlocks.CRYO_CHAMBER);

    private ModItems() {
    }
}
