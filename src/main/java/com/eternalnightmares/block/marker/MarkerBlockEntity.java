package com.eternalnightmares.block.marker;

import com.eternalnightmares.EternalNightmares;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MarkerBlockEntity extends BlockEntity {
    public MarkerBlockEntity(BlockPos pos, BlockState state) {
        super(EternalNightmares.marker_block_entity.get(), pos, state);
    }
}
