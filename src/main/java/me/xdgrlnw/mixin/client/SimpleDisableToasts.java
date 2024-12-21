package me.xdgrlnw.mixin.client;

import me.xdgrlnw.config.ClientConfig;
import net.minecraft.client.toast.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class SimpleDisableToasts {

    @Inject(method = "add(Lnet/minecraft/client/toast/Toast;)V", at = @At("HEAD"), cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo ci) {
        if (shouldDisableToast(toast)) {
            ci.cancel();
        }
    }

    @Unique
    private boolean shouldDisableToast(Toast toast) {
        return (ClientConfig.values.disableAnnoyingToasts &&
                toast instanceof SystemToast || toast instanceof RecipeToast || toast instanceof TutorialToast);
    }
}
