package me.gemini.smoothlagfix.mixin;

import net.minecraft.entity.ai.brain.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class BrainMixin {
    private int tickCount = 0;

    // Optimization: Throttling Brain (AI) ticking to reduce CPU usage
    // Most AI tasks don't need to run every single tick
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        tickCount++;
        if (tickCount % 2 != 0) {
            // Skip every 2nd tick for non-critical AI logic
            // In a full implementation, we would check if the entity is hostile or near a player
            ci.cancel();
        }
    }
}
