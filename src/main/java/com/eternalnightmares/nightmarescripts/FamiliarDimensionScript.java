package com.eternalnightmares.nightmarescripts;

import com.eternalnightmares.EternalNightmares;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FamiliarDimensionScript implements Nightmare {
    public static final NightmareData DEF  = new NightmareData(
            "eternalnightmares:familiar",
            30,
            false,
            false,
            130
    );

    @Override
    public NightmareData data() {
        return DEF;
    }

    @Override
    public void Main(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 5, 1, false, false, false));
        player.playNotifySound(EternalNightmares.sweden_reversed.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        player.getAbilities().invulnerable = true;
        player.onUpdateAbilities();
    }
}
