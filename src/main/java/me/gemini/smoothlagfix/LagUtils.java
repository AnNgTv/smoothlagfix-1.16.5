package me.gemini.smoothlagfix;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import java.util.concurrent.atomic.AtomicInteger;

public class LagUtils {
    
    public static int clearItems(MinecraftServer server) {
        AtomicInteger count = new AtomicInteger();
        for (ServerWorld world : server.getWorlds()) {
            world.iterateEntities().forEach(entity -> {
                if (entity instanceof ItemEntity) {
                    entity.remove();
                    count.getAndIncrement();
                }
            });
        }
        return count.get();
    }

    public static void broadcastClear(MinecraftServer server, int count) {
        if (count > 0) {
            server.getPlayerManager().broadcastChatMessage(
                new LiteralText("§6[SmoothLagFix] §eCleared §b" + count + " §eitems from the ground!"),
                false
            );
        }
    }
}
