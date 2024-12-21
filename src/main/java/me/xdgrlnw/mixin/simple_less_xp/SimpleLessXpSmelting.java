package me.xdgrlnw.mixin.simple_less_xp;

import me.xdgrlnw.config.CommonConfig;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class SimpleLessXpSmelting {
    @Inject(method = "dropExperience", at = @At("HEAD"), cancellable = true)
    private static void disableExp(CallbackInfo ci) {
        if (CommonConfig.values.disableSmeltingXp) ci.cancel();
    }
}