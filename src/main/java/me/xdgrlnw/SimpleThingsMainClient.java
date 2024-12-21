package me.xdgrlnw.client;

import me.xdgrlnw.client.util.SimpleToggles;
import net.fabricmc.api.ClientModInitializer;

public class SimpleThingsMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SimpleToggles.init();
    }
}
