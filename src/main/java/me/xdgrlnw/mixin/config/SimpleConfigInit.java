package me.xdgrlnw.mixin.config;

import com.mojang.datafixers.DataFix;
import me.xdgrlnw.config.ClientConfig;
import me.xdgrlnw.config.CommonConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataFix.class)
public class SimpleConfigInit {

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void initializeConfigs(CallbackInfo ci) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) ClientConfig.load();
        CommonConfig.load();
    }
}
