package com.truespace.world;

import com.truespace.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Builds the player's starting cryo-bunker at the world spawn — the physical
 * link between the lore (the player wakes from cryosleep) and the game world.
 *
 * <p>The bunker is a small reinforced hut with the {@link ModBlocks#CRYO_CHAMBER}
 * in the centre and a starter chest (tools + the opening journal). It is built
 * once (detected by the presence of the chamber) and the player is teleported
 * into it only on their first login.
 */
public final class SpawnBunker {

    /** Half-width of the 9x9 footprint (offsets from -4 to +4). */
    private static final int RADIUS = 4;
    /** Ceiling height above the ground floor. */
    private static final int WALL_TOP = 4;

    private SpawnBunker() {
    }

    /** Ensures the bunker exists at world spawn and wakes a first-time player inside it. */
    public static void ensure(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        BlockPos spawn = level.getSharedSpawnPos();
        BlockPos land = findLand(level, spawn);
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, land.getX(), land.getZ());
        BlockPos chamber = new BlockPos(land.getX(), ground + 1, land.getZ());

        // Build only if the chamber is not there yet (idempotent).
        if (!level.getBlockState(chamber).is(ModBlocks.CRYO_CHAMBER.get())) {
            build(level, land.getX(), land.getZ(), ground);
        }

        // Wake the player inside the bunker exactly once.
        if (!player.getPersistentData().getBoolean("truespace:spawned")) {
            BlockPos wake = chamber.north(); // beside the chamber, still inside
            player.teleportTo(wake.getX() + 0.5, wake.getY(), wake.getZ() + 0.5);
            player.setRespawnPosition(level.dimension(), wake, 0.0f, false, false);
            player.getPersistentData().putBoolean("truespace:spawned", true);
        }
    }

    /** Finds a non-water, non-lava column near spawn (spiral ring search). */
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

    private static void build(ServerLevel level, int ax, int az, int ground) {
        // 1. Clear the 9x9x5 volume above ground (removes trees/grass).
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                for (int dy = 0; dy <= WALL_TOP; dy++) {
                    set(level, ax + dx, ground + dy, az + dz, Blocks.AIR);
                }
            }
        }

        // 2. Stone-brick floor.
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                set(level, ax + dx, ground, az + dz, Blocks.STONE_BRICKS);
            }
        }

        // 3. Polished-deepslate walls and ceiling.
        for (int dy = 1; dy <= WALL_TOP; dy++) {
            for (int dx = -RADIUS; dx <= RADIUS; dx++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                    boolean perimeter = dx == -RADIUS || dx == RADIUS || dz == -RADIUS || dz == RADIUS;
                    boolean ceiling = dy == WALL_TOP;
                    if (perimeter || ceiling) {
                        set(level, ax + dx, ground + dy, az + dz, Blocks.POLISHED_DEEPSLATE);
                    }
                }
            }
        }

        // 4. Two-high doorway on the +Z wall.
        set(level, ax, ground + 1, az + RADIUS, Blocks.AIR);
        set(level, ax, ground + 2, az + RADIUS, Blocks.AIR);

        // 5. The cryo-chamber in the centre.
        set(level, ax, ground + 1, az, ModBlocks.CRYO_CHAMBER.get());

        // 6. Starter chest (journal + tools) next to the chamber.
        BlockPos chestPos = new BlockPos(ax + 1, ground + 1, az);
        level.setBlock(chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH),
                Block.UPDATE_ALL);
        if (level.getBlockEntity(chestPos) instanceof RandomizableContainerBlockEntity chest) {
            chest.setItem(0, journal());
            chest.setItem(1, new ItemStack(Items.IRON_PICKAXE));
            chest.setItem(2, new ItemStack(Items.TORCH, 8));
            chest.setItem(3, new ItemStack(Items.BREAD, 3));
        }

        // 7. Torches in the four interior corners.
        int[] corners = {-3, 3};
        for (int cx : corners) {
            for (int cz : corners) {
                set(level, ax + cx, ground + 1, az + cz, Blocks.TORCH);
            }
        }
    }

    private static void set(ServerLevel level, int x, int y, int z, Block block) {
        level.setBlock(new BlockPos(x, y, z), block.defaultBlockState(), Block.UPDATE_ALL);
    }

    /** The opening journal — the in-game delivery of the story intro. */
    private static ItemStack journal() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag tag = book.getOrCreateTag();
        tag.putString("title", "Пробуждение");
        tag.putString("author", "Смотритель");
        ListTag pages = new ListTag();
        pages.add(StringTag.valueOf("Ты очнулся. Меня зовут Смотритель. Я — ИИ этого бункера."));
        pages.add(StringTag.valueOf("Пока ты спал, мир сгорел. Третья мировая... и всё, что было, ушло."));
        pages.add(StringTag.valueOf("На поверхности остались две силы: «Возрождение» и «Исход». Обе зовут тебя. Обе правы по-своему. И обе опасны."));
        pages.add(StringTag.valueOf("Начни с руды. Боксит лежит близко к поверхности. Из него получают алюминий — а с него начнётся дорога вверх."));
        pages.add(StringTag.valueOf("Возьми инструменты из сундука. И помни: не верь тому, кто обещает спасение дёшево."));
        tag.put("pages", pages);
        return book;
    }
}
