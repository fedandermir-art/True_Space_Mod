package com.truespace.init;

import com.truespace.TrueSpaceMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TrueSpaceMod.MODID);

    public static final DeferredItem<Item> ALUMINUM_INGOT = ITEMS.registerSimpleItem("aluminum_ingot");
    public static final DeferredItem<Item> CRUSHED_BAUXITE = ITEMS.registerSimpleItem("crushed_bauxite");
    public static final DeferredItem<Item> ALUMINA = ITEMS.registerSimpleItem("alumina");
    public static final DeferredItem<Item> CRYOLITE = ITEMS.registerSimpleItem("cryolite");
    public static final DeferredItem<Item> CARBON_ANODE = ITEMS.registerSimpleItem("carbon_anode");
    public static final DeferredItem<Item> RED_MUD = ITEMS.registerSimpleItem("red_mud");

    public static final DeferredItem<BlockItem> BAUXITE_ORE =
            ITEMS.registerSimpleBlockItem("bauxite_ore", ModBlocks.BAUXITE_ORE);

    public static final DeferredItem<BlockItem> CRYO_CHAMBER =
            ITEMS.registerSimpleBlockItem("cryo_chamber", ModBlocks.CRYO_CHAMBER);

    public static final DeferredItem<BlockItem> LAB_PANEL =
            ITEMS.registerSimpleBlockItem("lab_panel", ModBlocks.LAB_PANEL);

    public static final DeferredItem<BlockItem> BUNKER_WHITE_IRON =
            ITEMS.registerSimpleBlockItem("bunker_white_iron", ModBlocks.BUNKER_WHITE_IRON);

    private ModItems() {
    }
}
