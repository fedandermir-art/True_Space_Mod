package com.truespace.init;

import com.truespace.TrueSpaceMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * All mod blocks live here. BlockItems are registered in {@link ModItems}.
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TrueSpaceMod.MODID);

    public static final DeferredBlock<Block> BAUXITE_ORE = BLOCKS.registerSimpleBlock(
            "bauxite_ore",
            p -> p.mapColor(MapColor.TERRACOTTA_RED)
                    .requiresCorrectToolForDrops()
                    .strength(3.0f, 3.0f)
                    .sound(SoundType.STONE));

    public static final DeferredBlock<Block> CRYO_CHAMBER = BLOCKS.registerSimpleBlock(
            "cryo_chamber",
            p -> p.mapColor(MapColor.COLOR_GRAY)
                    .strength(4.0f, 1200.0f)
                    .sound(SoundType.METAL));

    public static final DeferredBlock<Block> LAB_PANEL = BLOCKS.registerSimpleBlock(
            "lab_panel",
            p -> p.mapColor(MapColor.COLOR_GRAY)
                    .requiresCorrectToolForDrops()
                    .strength(2.0f, 4.0f)
                    .sound(SoundType.METAL));

    /**
     * White striped iron bunker panel — белая полосатая как железо, для бункера.
     * Текстура сгенерирована tools/generate_textures.py (seed 1337).
     * Запрос от пользователя: "как железо в полоску белая"
     */
    public static final DeferredBlock<Block> BUNKER_WHITE_IRON = BLOCKS.registerSimpleBlock(
            "bunker_white_iron",
            p -> p.mapColor(MapColor.SNOW)
                    .requiresCorrectToolForDrops()
                    .strength(2.5f, 6.0f)
                    .sound(SoundType.METAL));

    private ModBlocks() {
    }
}
