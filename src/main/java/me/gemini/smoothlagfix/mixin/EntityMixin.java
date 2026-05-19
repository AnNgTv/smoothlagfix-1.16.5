package me.gemini.smoothlagfix.mixin;

import me.gemini.smoothlagfix.SmoothLagFix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public World world;
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();
    @Shadow public int age;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (!SmoothLagFix.CONFIG.enableEntityCulling) return;
        
        Entity entity = (Entity) (Object) this;
        
        // Don't cull players or critical entities
        if (entity instanceof PlayerEntity) return;
        
        if (world.isClient) return;
        
        double rangeSq = SmoothLagFix.CONFIG.cullingRange * SmoothLagFix.CONFIG.cullingRange;
        boolean playerNearby = false;
        
        // Optimized check: only check players in the same world
        for (PlayerEntity player : world.getPlayers()) {
            if (player.squaredDistanceTo(getX(), getY(), getZ()) < rangeSq) {
                playerNearby = true;
                break;
            }
        }
        
        if (!playerNearby) {
            // Skip 19 out of 20 ticks if no player is nearby
            if (age % 20 != 0) {
                ci.cancel();
            }
        }
    }
}
