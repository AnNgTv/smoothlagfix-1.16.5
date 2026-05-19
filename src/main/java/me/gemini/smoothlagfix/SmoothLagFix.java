package me.gemini.smoothlagfix;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmoothLagFix implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("smoothlagfix");

    private int tickCounter = 0;
    private static final int CLEAR_INTERVAL = 6000; // 5 minutes in ticks

    @Override
    public void onInitialize() {
        LOGGER.info("SmoothLagFix initialized! Optimizing your game...");
        
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.START_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter >= CLEAR_INTERVAL) {
                tickCounter = 0;
                int cleared = LagUtils.clearItems(server);
                LagUtils.broadcastClear(server, cleared);
            }
        });

        net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(net.minecraft.server.command.CommandManager.literal("lagclear")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    int cleared = LagUtils.clearItems(context.getSource().getMinecraftServer());
                    context.getSource().sendFeedback(new net.minecraft.text.LiteralText("§aManually cleared §b" + cleared + " §aitems."), true);
                    return 1;
                })
            );
        });
    }
}
