package me.xdgrlnw.client.mixin;

import me.xdgrlnw.client.config.ClientConfig;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class SimpleTitleChanger {

    @Inject(method = "getWindowTitle", at = @At("TAIL"), cancellable = true)
    private void setCustomWindowTitle(CallbackInfoReturnable<String> cir) {
        if (!ClientConfig.values.enableCustomTitle) return;

        String mcVersion = MinecraftVersion.create().getName();
        String customTitle = ClientConfig.values.customTitle;
        String formattedTitle = String.format("[ %s ] %s", mcVersion, customTitle);

        cir.setReturnValue(formattedTitle);
    }
}
