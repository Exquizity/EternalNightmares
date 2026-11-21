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

public class HappyHomeDimensionScript implements Nightmare {
    public static final NightmareData DEF = new NightmareData(
            "eternalnightmares:happyhome",
            30,
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

    }
}
