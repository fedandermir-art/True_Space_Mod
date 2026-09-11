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

    /**
     * Bauxite is a surficial laterite ore (formed by tropical weathering), so it
     * generates near the surface — see the worldgen files under
     * {@code src/main/resources/data/truespace/worldgen/}.
     * Mined with any pickaxe (no tier requirement).
     */
    public static final DeferredBlock<Block> BAUXITE_ORE = BLOCKS.registerSimpleBlock(
            "bauxite_ore",
            p -> p.mapColor(MapColor.TERRACOTTA_RED)
                    .requiresCorrectToolForDrops()
                    .strength(3.0f, 3.0f)
                    .sound(SoundType.STONE));

    /**
     * The cryo-chamber the player wakes up in — the physical link between the
     * lore (cryosleep) and the game world. Placed by {@link com.truespace.world.SpawnBunker}.
     */
    public static final DeferredBlock<Block> CRYO_CHAMBER = BLOCKS.registerSimpleBlock(
            "cryo_chamber",
            p -> p.mapColor(MapColor.COLOR_GRAY)
                    .strength(4.0f, 1200.0f)
                    .sound(SoundType.METAL));

    private ModBlocks() {
    }
}
