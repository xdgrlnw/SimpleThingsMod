package me.xdgrlnw.client.mixin;

import me.xdgrlnw.client.config.ClientConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;

@Mixin(InGameHud.class)
public abstract class SimpleHudOverlay {

    @Shadow @Final
    private MinecraftClient client;

    @Unique
    private static final Set<Item> LIGHT_ITEMS = Set.of(Items.TORCH, Items.LANTERN);
    @Unique
    private static final int COLOR = 0xFFFFFFFF;

    @Inject(method = "render", at = @At("TAIL"))
    public void renderSimpleHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!ClientConfig.values.enableSimpleHud || client.options.hudHidden) return;

        PlayerEntity player = client.player;
        ClientWorld world = client.world;
        if (player == null || world == null) return;

        int yOffset = 5;

        if (isHoldingItem(player, Set.of(Items.COMPASS, Items.RECOVERY_COMPASS))) {
            renderCompassInfo(context, player, yOffset);
        }
        if (isHoldingItem(player, Set.of(Items.CLOCK))) {
            renderClockInfo(context, world, yOffset);
            yOffset = 17;
        }
        if (isHoldingItem(player, LIGHT_ITEMS)) {
            renderLightInfo(context, player, world, yOffset);
        }
    }

    @Unique
    private void renderCompassInfo(DrawContext context, PlayerEntity player, int yOffset) {
        BlockPos pos = player.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Text facing = getDirection(player.getYaw());
        Text posText = Text.translatable("hud.compass.position", x, y, z, facing);

        String biomeString = player.getWorld().getBiome(pos).getIdAsString().replace(":", ".");
        Text biome = Text.translatable("biome.%s".formatted(biomeString));
        Text biomeText = Text.translatable("hud.compass.biome", biome);

        context.drawTextWithShadow(client.textRenderer, posText, 5, yOffset, COLOR);
        if (player.getLastDeathPos().isPresent() && isHoldingItem(player, Set.of(Items.RECOVERY_COMPASS))) {
            yOffset = 17;
            BlockPos posDeath = player.getLastDeathPos().get().pos();
            int deathX = posDeath.getX();
            int deathY = posDeath.getY();
            int deathZ = posDeath.getZ();
            Text posDeathText = Text.translatable("hud.compass.death_position", deathX, deathY, deathZ);

            context.drawTextWithShadow(client.textRenderer, posDeathText, 5, yOffset, COLOR);
            if (player.squaredDistanceTo(deathX, deathY, deathZ) < 2.0 * 2.0 ) player.setLastDeathPos(Optional.empty());
        }
        context.drawTextWithShadow(client.textRenderer, biomeText, 5, yOffset + 12, COLOR);
    }

    @Unique
    private void renderClockInfo(DrawContext context, ClientWorld world, int yOffset) {
        Text clockText = Text.translatable("hud.clock", (world.getTimeOfDay() / 24000), (getMoonPhase(world.getMoonPhase())));

        context.drawTextWithShadow(client.textRenderer, clockText, xOffset(clockText), yOffset, COLOR);
    }

    @Unique
    private void renderLightInfo(DrawContext context, PlayerEntity player, ClientWorld world, int yOffset) {
        BlockPos pos = player.getBlockPos();
        Text lightText = Text.translatable("hud.light", world.getLightLevel(LightType.BLOCK, pos), world.getLightLevel(LightType.SKY, pos));

        context.drawTextWithShadow(client.textRenderer, lightText, xOffset(lightText), yOffset, COLOR);
    }

    @Unique
    private static Text getDirection(float yaw) {
        String[] directions = {"s", "sw", "w", "nw", "n", "ne", "e", "se"};
        int index = Math.round((yaw % 360 + 360) % 360 / 45) % 8;
        return Text.translatable("hud.compass.direction." + directions[index]);
    }

    @Unique
    private static Text getMoonPhase(int phase) {
        return Text.translatable("hud.clock.moon_phase." + phase);
    }

    @Unique
    private static boolean isHoldingItem(PlayerEntity player, Set<Item> items) {
        return items.stream().anyMatch(item -> player.getMainHandStack().isOf(item) || player.getOffHandStack().isOf(item));
    }

    @Unique
    private int xOffset(Text text) {
        return client.getWindow().getScaledWidth() - 5 - client.textRenderer.getWidth(text);
    }
}
