package com.eternalnightmares.nightmares;

import com.eternalnightmares.EternalNightmares;
import com.eternalnightmares.nightmares.data.Nightmare;
import com.eternalnightmares.nightmares.data.NightmareData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class VoidDimensionScript implements Nightmare {

    public static final NightmareData DEF = new NightmareData(
            "eternalnightmares:dimensionvoid",
            30,
            true,
            false,
            50
    );

    @Override
    public NightmareData data() {
        return DEF;
    }

    @Override
    public void Main(ServerPlayer player) {
        player.playNotifySound(EternalNightmares.void_ambience.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        player.getAbilities().invulnerable = true;
        player.onUpdateAbilities();
    }
}