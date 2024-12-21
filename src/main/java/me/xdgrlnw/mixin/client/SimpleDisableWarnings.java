package me.xdgrlnw.client.mixin;

import com.mojang.serialization.Lifecycle;
import me.xdgrlnw.client.config.ClientConfig;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IntegratedServerLoader.class)
public abstract class SimpleDisableWarnings {

    @Redirect(
            method = "checkBackupAndStart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/SaveProperties;getLifecycle()Lcom/mojang/serialization/Lifecycle;"
            )
    )
    private Lifecycle redirectGetLifecycle(SaveProperties saveProperties) {
        return shouldDisableWarnings() ? Lifecycle.stable() : saveProperties.getLifecycle();
    }

    @ModifyVariable(
            method = "tryLoad",
            at = @At("HEAD"),
            argsOnly = true,
            index = 4
    )
    private static boolean modifyCreateWorldWarning(boolean original) {
        return shouldDisableWarnings();
    }

    @Unique
    private static boolean shouldDisableWarnings() {
        return ClientConfig.values.disableExperimentalWarning;
    }
}
