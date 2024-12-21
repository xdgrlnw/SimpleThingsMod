package me.xdgrlnw.mixin.simple_less_xp;

import me.xdgrlnw.config.CommonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreenHandler.class)
public class SimpleLessXpEnchanting {

    @Shadow @Final public int[] enchantmentPower;
    @Shadow @Final private Inventory inventory;
    @Inject(
            method = "method_17410(Lnet/minecraft/item/ItemStack;ILnet/minecraft/entity/player/PlayerEntity;ILnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyEnchantmentCosts(Lnet/minecraft/item/ItemStack;I)V")
    )
    private void fullEnchantCost(ItemStack itemStack, int level, PlayerEntity player, int cost, ItemStack itemStack2, World world, BlockPos pos, CallbackInfo ci) {
        if (!CommonConfig.values.enableFullEnchantCost) return;
        int reducedCost = enchantmentPower[level] - cost;
        player.applyEnchantmentCosts(inventory.getStack(0), reducedCost);
    }
}
