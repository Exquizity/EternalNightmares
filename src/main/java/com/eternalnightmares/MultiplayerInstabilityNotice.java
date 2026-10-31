package com.eternalnightmares;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = EternalNightmares.MODID)
public class MultiplayerInstabilityNotice {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        int count = player.server.getPlayerList().getPlayerCount();
        if (count >= 2) {
            for (ServerPlayer p : player.server.getPlayerList().getPlayers()) {
                p.displayClientMessage(Component.literal("The mod Eternal Nightmares has not been properly tested or yet developed for multiplayer. You may encounter bugs and glitches. You can make bug reports in the official Github page for the mod (https://github.com/Exquizity/EternalNightmares)"), false);
            }
        }
    }
}
