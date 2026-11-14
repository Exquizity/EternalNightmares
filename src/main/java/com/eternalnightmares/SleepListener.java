package com.eternalnightmares;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.eternalnightmares.util.NightmareUtil;

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

        if (player.getRandom().nextInt(100) < Config.NIGHTMARE_CHANCE.get()) { // If NightmareChance is higher than a random int from 1-100 then start nightmare
            LOGGER.info("Chance for nightmare failed");
        } else {
            LOGGER.info("Starting nightmare");
            NightmareUtil.StartNightmareForPlayer(player);
        }
    }
}