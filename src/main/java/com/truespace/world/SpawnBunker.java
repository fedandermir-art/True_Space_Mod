package com.truespace.world;

import com.truespace.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
 * in the centre and a starter chest (tools). It is built once (detected by the
 * presence of the chamber) and the player is woken inside it only on their
 * first login.
 *
 * <p>Minecraft 1.21.5+ API notes: {@code ServerPlayer#serverLevel()} became
 * {@code level()} (returns {@code ServerLevel}); {@code getSharedSpawnPos()} was
 * replaced by {@code getRespawnData()}, so the bunker is instead centred on the
 * player's first position (= world spawn for a new player); NBT getters return
 * {@code Optional}, so first-login is tracked with {@code contains()} +
 * {@code putBoolean()}; {@code teleportTo} now takes an extra {@code setCamera}
 * boolean.
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
        ServerLevel level = player.level();

        // A new player is placed at the world spawn, so their position is the
        // canonical bunker centre. A spiral land search handles ocean spawns.
        BlockPos land = findLand(level, player.blockPosition());
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, land.getX(), land.getZ());
        BlockPos chamber = new BlockPos(land.getX(), ground + 1, land.getZ());

        // Build only if the chamber is not there yet (idempotent).
        if (!level.getBlockState(chamber).is(ModBlocks.CRYO_CHAMBER.get())) {
            build(level, land.getX(), land.getZ(), ground);
        }

        // Wake the player inside the bunker exactly once.
        if (!player.getPersistentData().contains("truespace:spawned")) {
            BlockPos wake = chamber.north(); // beside the chamber, still inside
            player.teleportTo(wake.getX() + 0.5, wake.getY(), wake.getZ() + 0.5, true);
            player.getPersistentData().putBoolean("truespace:spawned", true);
            intro(player);
        }
    }

    /** The opening words of the Caretaker AI — in-game delivery of the story intro. */
    private static void intro(ServerPlayer player) {
        player.displayClientMessage(Component.literal("Смотритель: «Ты очнулся. Мир сгорел, но ты жив. Я — ИИ этого бункера.»"), false);
        player.displayClientMessage(Component.literal("Смотритель: «На поверхности две силы: «Возрождение» и «Исход». Обе правы. Обе опасны.»"), false);
        player.displayClientMessage(Component.literal("Смотритель: «Возьми инструменты из сундука. Начни с боксита у поверхности — с него начнётся дорога вверх.»"), false);
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

        // 6. Starter chest (tools) next to the chamber.
        BlockPos chestPos = new BlockPos(ax + 1, ground + 1, az);
        level.setBlock(chestPos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH),
                Block.UPDATE_ALL);
        if (level.getBlockEntity(chestPos) instanceof RandomizableContainerBlockEntity chest) {
            chest.setItem(0, new ItemStack(Items.IRON_PICKAXE));
            chest.setItem(1, new ItemStack(Items.TORCH, 8));
            chest.setItem(2, new ItemStack(Items.BREAD, 3));
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
}
