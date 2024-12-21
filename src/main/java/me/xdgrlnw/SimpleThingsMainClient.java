package me.xdgrlnw;

import me.xdgrlnw.util.SimpleToggles;
import net.fabricmc.api.ClientModInitializer;

public class SimpleThingsMainClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SimpleToggles.init();
    }
}
