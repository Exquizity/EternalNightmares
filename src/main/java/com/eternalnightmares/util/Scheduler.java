package com.eternalnightmares.util;

import com.eternalnightmares.EternalNightmares;
import java.util.ArrayList;
import java.util.List;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

// run code later
@EventBusSubscriber(modid = EternalNightmares.MODID)
public final class Scheduler
{
    private record Task(long runAtTick, Runnable action) {}
    private static final List<Task> TASKS = new ArrayList<>();
    private static long now = 0;

    public static void schedule(int ticks, Runnable action)
    {
        TASKS.add(new Task(now + ticks, action));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event)
    {
        now++;

        // take out everything that is due, then run it
        List<Task> due = new ArrayList<>();
        TASKS.removeIf(task ->
        {
            if (task.runAtTick() <= now)
            {
                due.add(task);
                return true;
            }
            return false;
        });
        due.forEach(task -> task.action().run());
    }

    // forget everything when the world is closed
    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event)
    {
        TASKS.clear();
        now = 0;
    }
}
