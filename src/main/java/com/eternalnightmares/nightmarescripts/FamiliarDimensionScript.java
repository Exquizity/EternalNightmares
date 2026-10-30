package com.eternalnightmares.nightmarescripts;

import com.eternalnightmares.EternalNightmares;
import com.eternalnightmares.util.NightmareUtil;
import com.eternalnightmares.util.Scheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class FamiliarDimensionScript implements Nightmare {
    public static final NightmareData DEF  = new NightmareData(
            "eternalnightmares:familiar",
            38,
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

        Level level = player.level();
        RandomSource random = player.getRandom();

        Scheduler.schedule(460, () -> {
            for (int cd = 9; cd <= 108; cd += 9) {
                final int delay = cd;

                Scheduler.schedule(delay, () -> {
                    LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);

                    if (delay == 108) {
                        BlockPos pos = player.blockPosition();
                        lightning.setVisualOnly(true);

                        if (player.level().canSeeSky(pos)) {
                            // strike the player and end the nightmare as a fail
                            lightning.moveTo(player.getX(), player.getY(), player.getZ());
                            if (player.level() instanceof ServerLevel serverLevel) {
                                serverLevel.broadcastEntityEvent(player, (byte) 2); // fake the player taking damage
                            }
                            Scheduler.schedule(2, () -> {
                                NightmareUtil.EndNightmareForPlayer(player, 0);
                            });
                        }
                    } else {
                        // random offset of 30-90 blocks from the player
                        int offsetX = (random.nextBoolean() ? 1 : -1) * (30 + random.nextInt(61));
                        int offsetZ = (random.nextBoolean() ? 1 : -1) * (30 + random.nextInt(61));

                        int x = player.blockPosition().getX() + offsetX;
                        int z = player.blockPosition().getZ() + offsetZ;
                        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z); // ground level at xz

                        // move to that offset
                        lightning.moveTo(x + 0.5, y, z + 0.5);
                    }

                    level.addFreshEntity(lightning); // adds the entity
                });
            }
        });
    }
}
