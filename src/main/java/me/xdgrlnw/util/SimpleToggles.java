package me.xdgrlnw.client.util;

import me.xdgrlnw.client.config.ClientConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class SimpleToggles {
    private static final KeyBinding TOGGLE_HUD_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.toggle.hud", InputUtil.Type.KEYSYM, 86, "key.category.simple_things")
    );

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (TOGGLE_HUD_KEY.wasPressed()) {
                toggleHudVisibility(client);
            }
        });
    }

    private static void toggleHudVisibility(net.minecraft.client.MinecraftClient client) {
        ClientConfig.values.enableSimpleHud = !ClientConfig.values.enableSimpleHud;
        ClientConfig.save();
        Text msgToggle = Text.translatable(
                ClientConfig.values.enableSimpleHud ? "key.toggle.hud.on" : "key.toggle.hud.off"
        );

        if (client.player != null) {
            client.player.sendMessage(msgToggle, true);
        }
    }
}
