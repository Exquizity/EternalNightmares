package com.eternalnightmares.block.triggers.staircasetrigger;

import com.eternalnightmares.EternalNightmares;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumSet;
import java.util.Properties;

public class StaircaseTrigger extends Block {
    public static final MapCodec<StaircaseTrigger> CODEC = simpleCodec(StaircaseTrigger::new);

    public StaircaseTrigger(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    // runs when player or entity is inside of the block
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) {
            return;
        }

        if (entity instanceof ServerPlayer player) {
            player.teleportTo(player.serverLevel(), player.getX(), player.getY() - 13, player.getZ(), player.getYRot(), player.getXRot());
            player.playNotifySound(EternalNightmares.warp.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }

    // doesnt render
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    // cant be targetted
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    // light and stuff goes through
    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0f;
    }
}
