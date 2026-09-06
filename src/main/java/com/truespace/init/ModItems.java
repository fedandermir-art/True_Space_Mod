package com.truespace.init;

import com.truespace.TrueSpaceMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All mod items are declared here, in one place, so the content stays
 * greppable and the design-time registry in {@code content/} can be
 * cross-checked against the code.
 *
 * <p>Rule of the project: every item added here MUST also have an entry in
 * {@code content/registry/items.json} (or its category file) with real-world
 * data and a source. Content without a real basis does not ship.
 */
public final class ModItems {

    /** Central DeferredRegister for items, registered against the mod event bus. */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TrueSpaceMod.MODID);

    // ==================================================================
    // Materials — metals (milestone 2 will build the full chain behind them)
    // ==================================================================

    /**
     * Proof-of-pipeline item and the first real material of the mod.
     * Eventually produced by Hall-Héroult electrolysis of alumina, not by hand.
     * See {@code content/registry/materials.json}.
     */
    public static final DeferredItem<Item> ALUMINUM_INGOT = ITEMS.registerSimpleItem("aluminum_ingot");

    // TODO(milestone 2): titanium, steel, copper, ... with their full realistic chains.

    private ModItems() {
    }
}
