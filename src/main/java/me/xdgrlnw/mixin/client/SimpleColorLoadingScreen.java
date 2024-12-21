package me.xdgrlnw.mixin.client;

import me.xdgrlnw.config.ClientConfig;
import me.xdgrlnw.util.SimpleLogger;
import net.minecraft.client.gui.screen.SplashOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
public abstract class SimpleColorLoadingScreen {
    @Mutable
    @Shadow @Final private static int MOJANG_RED;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void changeColor(CallbackInfo ci) {
        if (!ClientConfig.values.enableBootScreenReColor) return;
        String hexColor = ClientConfig.values.bootScreenColor;
        if (hexColor != null && hexColor.startsWith("#") && hexColor.length() == 7) {
            MOJANG_RED = (int) Long.parseLong(hexColor.substring(1), 16);
        } else {
            SimpleLogger.logError("Invalid HEX color in configuration: " + hexColor);
        }
    }
}
