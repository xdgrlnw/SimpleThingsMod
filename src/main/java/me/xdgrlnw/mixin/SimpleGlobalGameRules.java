package me.xdgrlnw.mixin;

import me.xdgrlnw.config.CommonConfig;
import me.xdgrlnw.util.SimpleLogger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class SimpleGlobalGameRules {

    @Inject(method = "loadWorld", at = @At(value = "TAIL"))
    private void setServerGameRules(CallbackInfo info) {
        GameRules gameRules = ((MinecraftServer) (Object) this).getGameRules();
       applyServerGameRules(gameRules);
    }

    @Unique
    private static void applyServerGameRules(GameRules gameRules) {
        if (!CommonConfig.values.enableGlobalGameRules) return;
        gameRules.get(GameRules.DISABLE_ELYTRA_MOVEMENT_CHECK).set(CommonConfig.values.disableElytraMovementCheck, null);
        gameRules.get(GameRules.DISABLE_PLAYER_MOVEMENT_CHECK).set(CommonConfig.values.disablePlayerMovementCheck, null);
        gameRules.get(GameRules.REDUCED_DEBUG_INFO).set(CommonConfig.values.reducedDebugInfo, null);
        gameRules.get(GameRules.SPAWN_CHUNK_RADIUS).set(CommonConfig.values.spawnChunkRadius, null);
        gameRules.get(GameRules.PLAYERS_SLEEPING_PERCENTAGE).set(CommonConfig.values.playerSleepingPercentage, null);

        SimpleLogger.log("Server GameRules applied from config.");
    }
}
