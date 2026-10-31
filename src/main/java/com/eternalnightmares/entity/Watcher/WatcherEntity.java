package com.eternalnightmares.entity.Watcher;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WatcherEntity extends Monster {
    public WatcherEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        setSilent(true); // no footstep or hit sound
        setInvulnerable(true); // cant be hurt
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 500.0);
    }

    // This runs every tick (stare at player)
    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            return;
        }

        if (tickCount > 100) {
            discard(); // remove after entity has existed for 5 seconds (100 ticks)
        }

        Player target = level().getNearestPlayer(this, 64);
        if (target != null) {
            // this calculates how to look at the player
            double dx = target.getX() - getX();
            double dy = target.getEyeY() - getEyeY();
            double dz = target.getZ() - getZ();
            double flat = Math.sqrt(dx * dx + dz * dz);

            float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx))) - 90.0f;
            float pitch = (float) -Math.toDegrees(Math.atan2(dy, flat));

            setYRot(yaw);
            setXRot(pitch);
            setYHeadRot(yaw);
            setYBodyRot(yaw);
        }
    }
}
