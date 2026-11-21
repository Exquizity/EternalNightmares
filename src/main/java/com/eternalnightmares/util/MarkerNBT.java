package com.eternalnightmares.util;

import com.eternalnightmares.EternalNightmares;
import com.eternalnightmares.block.marker.MarkerBlock;
import com.eternalnightmares.block.marker.MarkerBlockEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class MarkerNBT {
    public static final Logger LOGGER = LogUtils.getLogger();

    private static boolean IsMarker(ServerLevel level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MarkerBlockEntity;
    }

    public static String GetString(ServerPlayer player, BlockPos pos, String nbt) {
        ServerLevel level = player.serverLevel();

        if (!IsMarker(level, pos)) {
            LOGGER.error("Marker doesnt exist");
            return "";
        }

        MarkerBlockEntity marker = (MarkerBlockEntity) level.getBlockEntity(pos);
        return marker.getPersistentData().getString(nbt);
    }

    public static int GetInt(ServerPlayer player, BlockPos pos, String nbt) {
        ServerLevel level = player.serverLevel();

        if (!IsMarker(level, pos)) {
            LOGGER.error("Marker doesnt exist");
            return -1;
        }

        MarkerBlockEntity marker = (MarkerBlockEntity) level.getBlockEntity(pos);
        return marker.getPersistentData().getInt(nbt);
    }
}
