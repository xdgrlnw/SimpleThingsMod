package me.xdgrlnw.mixin.config;

import me.xdgrlnw.config.CommonConfig;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class SimpleConfigInit {

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void initializeConfigs(CallbackInfo ci) {CommonConfig.load();}
}
