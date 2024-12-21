package me.xdgrlnw.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xdgrlnw.util.SimpleLogger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientConfig {

    private static final String CONFIG_DIR = "config/simple_things/";
    private static final String CLIENT_CONFIG_FILE = "client_config.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ConfigValues values = new ConfigValues();

    public static void load() {
        File file = new File(CONFIG_DIR + CLIENT_CONFIG_FILE);
        if (file.exists()) {
            try (Reader reader = Files.newBufferedReader(Paths.get(file.getPath()))) {
                values = GSON.fromJson(reader, ConfigValues.class);
                SimpleLogger.log("Client config loaded.");
            } catch (IOException e) {
                SimpleLogger.logError("Failed to load client config: " + e.getMessage());
            }
        } else {
            SimpleLogger.log("Client config not found. Creating a new one...");
            save();
        }
    }

    public static void save() {
        try {
            File dir = new File(CONFIG_DIR);
            if (!dir.exists() && !dir.mkdirs()) {
                SimpleLogger.logError("Failed to create config directory.");
                return;
            }

            try (Writer writer = Files.newBufferedWriter(Paths.get(CONFIG_DIR + CLIENT_CONFIG_FILE))) {
                GSON.toJson(values, writer);
                SimpleLogger.log("Client config saved.");
            }
        } catch (IOException e) {
            SimpleLogger.logError("Failed to save client config: " + e.getMessage());
        }
    }

    public static class ConfigValues {
        String CONFIG_HUD = "--- HUD ---";
        public boolean enableSimpleHud = true;
        public boolean enableDurabilityTooltip = true;
        String CONFIG_FIXES = "--- FIXES ---";
        public boolean disableAnnoyingToasts = true;
        public boolean disableExperimentalWarning = true;
        public boolean disableAutoFullScreen = true;
        String CONFIG_CUSTOMIZATION = "--- CUSTOMIZATION ---";
        public boolean enableCustomTitle = true;
        public boolean enableBootScreenReColor = true;
        public String bootScreenColor = "#FFFF55";
        public String customTitle = "Minecraft - Simple Vanilla - ModPack";
    }
}
