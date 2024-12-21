package me.xdgrlnw.mixin.simple_less_xp;

import me.xdgrlnw.config.CommonConfig;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FishingBobberEntity.class)
public abstract class SimpleLessXpFishing {

    @Inject(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getWorld()Lnet/minecraft/world/World;", ordinal = 1),
            cancellable = true
    )
    private void disableExp(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        if (!CommonConfig.values.disableFishingXp) return;
        FishingBobberEntity bobberEntity = (FishingBobberEntity) (Object) this;
        PlayerEntity player = bobberEntity.getPlayerOwner();

        if (player == null || player.getWorld().isClient) return;

        World world = player.getWorld();
        List<ItemStack> loot = generateLootForFishing();

        loot.forEach(itemStack -> {
            spawnItemEntity(world, player, itemStack);
            if (itemStack.isIn(ItemTags.FISHES)) {
                player.increaseStat(Stats.FISH_CAUGHT, 1);
            }
        });

        bobberEntity.discard();
        cir.setReturnValue(1);
    }

    @Unique
    private void spawnItemEntity(World world, PlayerEntity player, ItemStack itemStack) {
        ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), itemStack);
        itemEntity.setVelocity(0, 0.2, 0);
        world.spawnEntity(itemEntity);
    }

    @Unique
    private List<ItemStack> generateLootForFishing() {
        return List.of();
    }
}
