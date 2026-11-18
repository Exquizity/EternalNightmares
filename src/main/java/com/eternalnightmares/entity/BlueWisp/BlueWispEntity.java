package com.eternalnightmares.entity.BlueWisp;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BlueWispEntity extends Monster {
    private static final double APPROACH_RANGE = 40.0;
    private static final double LEVITATE_RANGE = 15.0;
    private static final double SPACING = 35.0;
    private static final float WANDERING_SPD = 0.05f;
    private static final float APPROACH_SPEED = 0.09f;

    private int headingTimer;
    private float heading;

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FOLLOW_RANGE, 40);
    }

    public BlueWispEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        this.setNoGravity(true);
        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        if (this.removeNearby()) {
            return;
        }

        this.raiseNearbyPlayers();

        Player target = this.level().getNearestPlayer(this, APPROACH_RANGE);

        if (target != null) {
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 1.0E-4) {
                this.setDeltaMovement(dx / distance * APPROACH_SPEED, 0.0, dz / distance * APPROACH_SPEED);
                return;
            }
        }

        if (--this.headingTimer <= 0) {
            this.heading = this.getRandom().nextFloat() * ((float) Math.PI * 2f);
            this.headingTimer = 60 + this.getRandom().nextInt(120);
        }

        this.setDeltaMovement(
                Math.cos(this.heading) * WANDERING_SPD,
                0.0,
                Math.sin(this.heading) * WANDERING_SPD);
    }

    private boolean removeNearby() {
        if (this.getRandom().nextInt(3) != 0) {
            return false;
        }

        for (BlueWispEntity other : this.level().getEntitiesOfClass(BlueWispEntity.class, this.getBoundingBox().inflate(SPACING))) {
            if (other != this && this.distanceToSqr(other) <= SPACING * SPACING) {
                this.discard();
                return true;
            }
        }

        return false;
    }

    private void raiseNearbyPlayers() {
        for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(LEVITATE_RANGE))) {
            if (player.isSpectator()) {
                continue;
            }

            MobEffectInstance current = player.getEffect(MobEffects.LEVITATION);

            if (current == null || current.getDuration() < 10) {
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 3, 0, false, false, false));
            }
        }
    }
}
