package me.xdgrlnw.mixin;

import me.xdgrlnw.util.SimpleLogger;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.common.ServerTransferS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

@Mixin(AbstractSignBlock.class)
public abstract class SimpleServerWarp {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onSignUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!player.isSneaking()) {
            if (world.isClient || !(player instanceof ServerPlayerEntity serverPlayer)) {
                return;
            }

            String defaultAddress = Objects.requireNonNull(serverPlayer.getServer()).getServerIp();
            int defaultPort = serverPlayer.getServer().getServerPort();

            SignBlockEntity signEntity = (SignBlockEntity) world.getBlockEntity(pos);
            if (signEntity == null) {
                return;
            }

            Text[] messages = signEntity.getFrontText().getMessages(true);
            if (messages.length < 3 || !isWarpSign(messages)) {
                return;
            }

            String address = messages[2].getString().trim();
            int port = parsePort(messages[3]);

            if (port == -1 || !attemptWarp(address, port)) {
                handleWarp(serverPlayer, defaultAddress, defaultPort, player, false);
                cir.setReturnValue(ActionResult.FAIL);
            } else {
                handleWarp(serverPlayer, address, port, player, true);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }

    @Unique
    private boolean isWarpSign(Text[] messages) {
        return "[WARP]".equalsIgnoreCase(messages[1].getString());
    }

    @Unique
    private int parsePort(Text portText) {
        try {
            return Integer.parseInt(portText.getString().trim());
        } catch (NumberFormatException e) {
            SimpleLogger.logError("Failed to parse port: " + portText.getString());
            return -1;
        }
    }

    @Unique
    private boolean attemptWarp(String address, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(address, port), 5000);
            return true;
        } catch (IOException e) {
            SimpleLogger.logError("Warp failed for address: " + address + " and port: " + port);
            return false;
        }
    }

    @Unique
    private void handleWarp(ServerPlayerEntity serverPlayer, String address, int port, PlayerEntity player, boolean success) {
        serverPlayer.networkHandler.sendPacket(new ServerTransferS2CPacket(address, port));
        Text message = success
                ? Text.translatable("text.warp.success", address, port) : Text.translatable("text.warp.fail");
        player.sendMessage(message, false);
        SimpleLogger.log(success ? "Warp successful to " + address + ":" + port : "Warp failed to " + address + ":" + port);
    }
}
