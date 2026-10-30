package com.eternalnightmares.util;

import com.eternalnightmares.EternalNightmares;
import com.eternalnightmares.nightmarescripts.FamiliarDimensionScript;
import com.eternalnightmares.nightmarescripts.VoidDimensionScript;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import org.slf4j.Logger;
import com.eternalnightmares.nightmarescripts.Nightmare;

import java.util.List;
import net.minecraft.world.level.Level;

public class NightmareUtil {
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final List<Nightmare> NIGHTMARES = List.of( // note to self, i need to add to the list WHEN I ADD A NEW NIGHTMARE
            new VoidDimensionScript(),
            new FamiliarDimensionScript()

    );

    public static void NightmareResultActionForPlayer(ServerPlayer player, int type) {
        FoodData food = player.getFoodData();
        if (type == 0) { // fail
            //this down here plays the heartbeat
            int val = 20;
            while (!(val == 180)) {
                Scheduler.schedule(val, () -> {
                    player.playNotifySound(EternalNightmares.heartbeat.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                });
                val = val + 20;

            }
            // effects
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 2, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 50, 2, false, false, false));
            food.setFoodLevel(Math.max(0, food.getFoodLevel() - 9)); // removes 4.5 bars of hunger
        } else if (type == 2) { // win
            food.setFoodLevel(Math.max(0, food.getFoodLevel() + 6));
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 2, 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1, false, false, false));
        }
    }

    public static void EndNightmareForPlayer(ServerPlayer player, int type) {
        // type is how the nightmare ends, 0 is lost, 1 is neutral, 2 is won
        BlockPos pos = player.getRespawnPosition();
        if (pos == null) {
            return;
        }

        player.teleportTo(player.server.overworld(), pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
        player.getAbilities().invulnerable = false;
        player.onUpdateAbilities();
        NightmareResultActionForPlayer(player, type);
    };

    public static void NightmareTimeoutForPlayer(ServerPlayer player, Nightmare selected) {
        if (!player.level().dimension().location().toString().equals(selected.data().target())) { // (if players current dimension != the nightmare data's target)
            return;
        };

        int type = 0; // default is loss
        if (selected.data().neutral()) { // is nightmare neutral
            type = 1;
        } else {
            if (!selected.data().timeoutIsLoss()) { // is timeoutIsLoss false
                type = 2;
            }
        }

        EndNightmareForPlayer(player, type);
    };

    public static void StartNightmareForPlayer(ServerPlayer player) {
        if (NIGHTMARES.isEmpty()) { // this should never happen anyway
            return;
        }

        Nightmare selected = NIGHTMARES.get(player.getRandom().nextInt(NIGHTMARES.size()));
        LOGGER.info("Selected Nightmare: ", selected.getClass().getSimpleName());

        // take the player to the dimension after 1 tick after waking up
        Scheduler.schedule(1, () -> {

            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(selected.data().target()));
            ServerLevel dimension = player.server.getLevel(key);
            if (dimension == null) {
                LOGGER.error("nightmare dimension was null");
                return;
            }
            // take player to the dimension
            player.teleportTo(dimension, player.getX(), selected.data().spawnY(), player.getZ(), player.getYRot(), player.getXRot());

            selected.Main(player); // starts the nightmares main func

            Scheduler.schedule(selected.data().duration() * 20, () -> { // schedule NightmareTimeoutForPlayer after nightmare data duration * 20 (ticks)
                NightmareTimeoutForPlayer(player, selected);
            });
        });

    }
}
