package com.eternalnightmares.server;

import com.eternalnightmares.Config;
import com.eternalnightmares.EternalNightmares;
import com.eternalnightmares.util.NightmareCore;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@EventBusSubscriber(modid = EternalNightmares.MODID)
public class SleepListener {
    public static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void PlayerWakesUp(PlayerWakeUpEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) { // it has to be a server player
            return;
        }

        LOGGER.info("Player woke up.");

        if (!Config.NIGHTMARES_ENABLED.get()) {
            LOGGER.info("Nightmares cannot trigger because the config has it off");
            return;
        }

        if (player.getRandom().nextInt(100) < Config.NIGHTMARE_CHANCE.get()) { // roll below chance % -> nightmare starts
            LOGGER.info("Nightmare chance roll succeeded");
            NightmareCore.StartNightmareForPlayer(player);
        } else {
            LOGGER.info("Nightmare chance roll failed");
        }
    }
}