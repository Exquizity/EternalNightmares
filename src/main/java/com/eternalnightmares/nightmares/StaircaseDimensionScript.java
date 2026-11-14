package com.eternalnightmares.nightmares;

import com.eternalnightmares.EternalNightmares;
import com.eternalnightmares.nightmares.data.Nightmare;
import com.eternalnightmares.nightmares.data.NightmareData;
import com.eternalnightmares.util.Scheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

import java.util.Optional;

public class StaircaseDimensionScript implements Nightmare {
    public static final NightmareData DEF = new NightmareData(
            "eternalnightmares:staircase",
            45,
            true,
            false,
            100
    );

    @Override
    public NightmareData data() {
        return DEF;
    }

    @Override
    public void Main(ServerPlayer player) {
        Scheduler.schedule(5, () -> {
            ServerLevel level = player.serverLevel();

            Optional<BlockPos> target = BlockPos.findClosestMatch(
                    player.blockPosition(),
                    200, 100,
                    pos -> level.getBlockState(pos).is(EternalNightmares.staircasetrigger.get())
            );

            if (target.isEmpty()) {
                EternalNightmares.LOGGER.warn("staircase nightmare: no staircasetrigger found");
                return;
            }

            BlockPos pos = target.get();
            player.teleportTo(level, pos.getX(), pos.getY() - 13, pos.getZ(), player.getYRot(), player.getXRot());
            Scheduler.schedule(20, () -> {
                player.playNotifySound(EternalNightmares.staircase_ambience.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            });
        });
    }
}
