package com.truespace.world;

import com.truespace.TrueSpaceMod;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Game-bus events that tie the world to the lore.
 *
 * <p>NeoForge 21.1: {@code @EventBusSubscriber} no longer has a {@code bus}
 * attribute — {@code PlayerLoggedInEvent} is not an {@code IModBusEvent}, so it
 * is automatically registered to the game bus.
 */
@EventBusSubscriber(modid = TrueSpaceMod.MODID)
public final class SpawnHandler {

    private SpawnHandler() {
    }

    /**
     * On every login: make sure the underground spawn cell exists at world spawn
     * and, on the player's very first login, wake them inside it.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpawnSite.setup(player);
        }
    }
}
