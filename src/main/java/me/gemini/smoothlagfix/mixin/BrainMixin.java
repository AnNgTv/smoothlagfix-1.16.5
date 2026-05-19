package me.gemini.smoothlagfix.mixin;

import net.minecraft.entity.ai.brain.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class BrainMixin {
    private int tickCount = 0;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (!me.gemini.smoothlagfix.SmoothLagFix.CONFIG.enableBrainThrottling) return;

        tickCount++;
        if (tickCount % 2 != 0) {
            ci.cancel();
        }
    }
}
