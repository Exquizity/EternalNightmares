package com.eternalnightmares.nightmares.data;

import net.minecraft.server.level.ServerPlayer;

public interface Nightmare {
    void Main(ServerPlayer player);
    NightmareData data();
}