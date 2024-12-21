package me.xdgrlnw.mixin.simple_less_xp;

import me.xdgrlnw.config.CommonConfig;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class SimpleLessXpMining {
    @Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
    private void disableExp(CallbackInfo ci) {
        if (CommonConfig.values.disableMiningXp) ci.cancel();
    }
}