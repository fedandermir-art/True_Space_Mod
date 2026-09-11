package com.truespace.world;

import com.truespace.TrueSpaceMod;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Game-bus events that tie the world to the lore.
 */
@EventBusSubscriber(modid = TrueSpaceMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class SpawnHandler {

    private SpawnHandler() {
    }

    /**
     * On every login: make sure the cryo-bunker exists at world spawn and, on
     * the player's very first login, wake them inside it.
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SpawnBunker.ensure(player);
        }
    }
}
