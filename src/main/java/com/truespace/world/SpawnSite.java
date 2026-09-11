package com.truespace.world;

import com.truespace.TrueSpaceMod;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Prepares the player's starting point at world spawn — the playable opening of
 * the lore ("the player wakes from cryosleep underground").
 *
 * <p><b>What this does (and does not) do:</b>
 * <ul>
 *   <li>On first login the player wakes <b>underground</b> at the world-spawn
 *       column, inside a small pre-carved, sealed safe cell.</li>
 *   <li>A chest in the cell holds the Caretaker's note (a written book) and a
 *       starter kit.</li>
 *   <li>The bunker itself is <b>not</b> generated — the player builds it. This
 *       class only guarantees a safe, sealed underground spawn cell.</li>
 *   <li>Mineshaft generation is disabled globally (see
 *       {@code data/minecraft/tags/worldgen/biome/has_structure/mineshaft*.json}),
 *       so no mineshaft can intersect the spawn area.</li>
 * </ul>
 *
 * <p>Minecraft 1.21.5+ API notes: {@code ServerPlayer#serverLevel()} became
 * {@code level()} (returns {@code ServerLevel}); NBT getters return
 * {@code Optional}, so first-login is tracked with {@code contains()} +
 * {@code putBoolean()}; {@code teleportTo(x, y, z)} has no extra args in 21.11;
 * written books are built with {@link WrittenBookContent} +
 * {@code DataComponents.WRITTEN_BOOK_CONTENT} (no NBT tags).
 */
public final class SpawnSite {

    /** How many blocks below the surface the spawn cell sits. */
    private static final int DEPTH_BELOW_SURFACE = 8;
    /** Room is 3x3x3 (offsets -1..1 around the centre). */
    private static final int RADIUS = 1;

    private SpawnSite() {
    }

    /** Ensures the underground spawn cell exists and wakes a first-time player inside it. */
    public static void setup(ServerPlayer player) {
        ServerLevel level = player.level();

        // A brand-new player is placed at the world spawn, so their position is
        // the canonical bunker column. A spiral land search handles ocean spawns.
        BlockPos anchor = findLand(level, player.blockPosition());
        int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, anchor.getX(), anchor.getZ());
        int floorY = surface - DEPTH_BELOW_SURFACE; // this layer stays solid = the floor

        BlockPos centre = new BlockPos(anchor.getX(), floorY + 1, anchor.getZ()); // where the player stands
        BlockPos chestPos = new BlockPos(anchor.getX() + 1, floorY + 1, anchor.getZ()); // on the floor

        // Build the cell only if the chest is not there yet (idempotent).
        if (!level.getBlockState(chestPos).is(Blocks.CHEST)) {
            carveCell(level, centre);
            level.setBlock(chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH),
                    Block.UPDATE_ALL);
            if (level.getBlockEntity(chestPos) instanceof RandomizableContainerBlockEntity chest) {
                chest.setItem(0, caretakerNote());
                chest.setItem(1, new ItemStack(Items.IRON_PICKAXE));
                chest.setItem(2, new ItemStack(Items.TORCH, 8));
                chest.setItem(3, new ItemStack(Items.BREAD, 3));
            }
            // A torch on the wall so the cell isn't pitch black.
            level.setBlock(new BlockPos(anchor.getX(), floorY + 1, anchor.getZ() - 1),
                    Blocks.TORCH.defaultBlockState(), Block.UPDATE_ALL);
        }

        // Wake the player inside the cell exactly once.
        if (!player.getPersistentData().contains(TrueSpaceMod.MODID + ":spawned")) {
            player.teleportTo(centre.getX() + 0.5, centre.getY(), centre.getZ() + 0.5);
            player.getPersistentData().putBoolean(TrueSpaceMod.MODID + ":spawned", true);
        }
    }

    /**
     * Carves a 3x3x3 air cavity around the centre and seals its shell with stone
     * so a nearby cave, water or lava pocket can't leak into the spawn cell.
     */
    private static void carveCell(ServerLevel level, BlockPos centre) {
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -RADIUS; dy <= RADIUS; dy++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                    level.setBlock(centre.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        }
        // Seal the 5x5x5 shell (the ring one block outside the cavity).
        for (int dx = -RADIUS - 1; dx <= RADIUS + 1; dx++) {
            for (int dy = -RADIUS - 1; dy <= RADIUS + 1; dy++) {
                for (int dz = -RADIUS - 1; dz <= RADIUS + 1; dz++) {
                    if (Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz)) != RADIUS + 1) {
                        continue;
                    }
                    BlockPos p = centre.offset(dx, dy, dz);
                    var state = level.getBlockState(p);
                    if (state.isAir() || state.liquid()) {
                        level.setBlock(p, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
        }
    }

    /** The Caretaker's opening note — the lore intro, delivered as a book in the chest. */
    private static ItemStack caretakerNote() {
        ItemStack note = new ItemStack(Items.WRITTEN_BOOK);
        List<Filterable<Component>> pages = List.of(
                Filterable.passThrough(Component.literal("Смотритель: «Ты очнулся. Мир сгорел, но ты жив. Я — ИИ этого бункера.»")),
                Filterable.passThrough(Component.literal("«На поверхности две силы: «Возрождение» и «Исход». Обе правы. Обе опасны.»")),
                Filterable.passThrough(Component.literal("«Возьми инструменты из сундука. Начни с боксита у поверхности — с него начнётся дорога вверх.»")));
        WrittenBookContent content = new WrittenBookContent(
                Filterable.passThrough("Записка Смотрителя"),
                "Смотритель",
                0,
                pages,
                true);
        note.set(DataComponents.WRITTEN_BOOK_CONTENT, content);
        note.set(DataComponents.CUSTOM_NAME, Component.literal("Записка Смотрителя"));
        return note;
    }

    /** Finds a non-water, non-lava column near the given position (spiral ring search). */
    private static BlockPos findLand(ServerLevel level, BlockPos spawn) {
        if (isLand(level, spawn)) {
            return spawn;
        }
        for (int r = 1; r <= 8; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != r) {
                        continue; // only the ring, not the filled square
                    }
                    BlockPos p = spawn.offset(dx, 0, dz);
                    if (isLand(level, p)) {
                        return p;
                    }
                }
            }
        }
        return spawn;
    }

    private static boolean isLand(ServerLevel level, BlockPos p) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, p.getX(), p.getZ());
        var state = level.getBlockState(new BlockPos(p.getX(), y, p.getZ()));
        return !state.is(Blocks.WATER) && !state.is(Blocks.LAVA);
    }
}
