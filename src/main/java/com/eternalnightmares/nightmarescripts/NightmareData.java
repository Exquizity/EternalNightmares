package com.eternalnightmares.nightmarescripts;

public record NightmareData(String target,
                            int duration,
                            boolean neutral,
                            boolean timeoutIsLoss,
                            int spawnY) {}

// i dont care if this looks horrible atleast i can understand it