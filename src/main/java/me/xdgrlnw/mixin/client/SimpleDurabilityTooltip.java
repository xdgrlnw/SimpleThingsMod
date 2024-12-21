package me.xdgrlnw.mixin.client;

import me.xdgrlnw.config.ClientConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class SimpleDurabilityTooltip {

    @Inject(method = "getTooltip", at = @At("RETURN"))
    public void appendDurabilityTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        if (!ClientConfig.values.enableDurabilityTooltip) return;
        ItemStack itemStack = (ItemStack) (Object) this;

        if (itemStack.isDamageable()) {
            List<Text> tooltip = cir.getReturnValue();
            appendDurability(tooltip, itemStack);
        }
    }

    @Unique
    private void appendDurability(List<Text> tooltip, ItemStack itemStack) {
        int maxDurability = itemStack.getMaxDamage();
        int currentDurability = maxDurability - itemStack.getDamage();

        tooltip.add(Text.empty());
        tooltip.add(Text.translatable("item.durability", currentDurability, maxDurability));
    }
}
