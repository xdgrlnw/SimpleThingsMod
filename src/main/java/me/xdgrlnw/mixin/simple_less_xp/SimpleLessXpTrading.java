package me.xdgrlnw.mixin.simple_less_xp;

import me.xdgrlnw.config.CommonConfig;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TradeOffer.class)
public abstract class SimpleLessXpTrading {
    @Inject(method =
            "shouldRewardPlayerExperience",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void disableExp(CallbackInfoReturnable<Boolean> cir) {
        if (CommonConfig.values.disableTradingXp) cir.setReturnValue(false);
    }
}