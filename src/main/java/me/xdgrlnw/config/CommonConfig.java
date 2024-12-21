package me.xdgrlnw.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xdgrlnw.util.SimpleLogger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommonConfig {
    private static final String CONFIG_DIR = "config/simple_things/";
    private static final String COMMON_CONFIG_FILE = "common_config.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static CommonValues values = new CommonValues();

    public static void load() {
        File file = new File(CONFIG_DIR + COMMON_CONFIG_FILE);
        if (file.exists()) {
            try (Reader reader = Files.newBufferedReader(Paths.get(file.getPath()))) {
                values = GSON.fromJson(reader, CommonValues.class);
                SimpleLogger.log("Common config loaded.");
            } catch (IOException e) {
                SimpleLogger.logError("Failed to load common config: " + e.getMessage());
            }
        } else {
            SimpleLogger.log("Common config not found. Creating a new one...");
            save();
        }
        updateLoggingStatus();
    }

    public static void save() {
        try {
            File dir = new File(CONFIG_DIR);
            if (!dir.exists() && !dir.mkdirs()) {
                SimpleLogger.logError("Failed to create config directory.");
                return;
            }

            try (Writer writer = Files.newBufferedWriter(Paths.get(CONFIG_DIR + COMMON_CONFIG_FILE))) {
                GSON.toJson(values, writer);
                SimpleLogger.log("Common config saved.");
            }
        } catch (IOException e) {
            SimpleLogger.logError("Failed to save common config: " + e.getMessage());
        }
    }

    private static void updateLoggingStatus() {
        boolean loggingEnabled = values.loggingEnabled;
        SimpleLogger.setLoggingEnabled(loggingEnabled);
    }

    public static class CommonValues {
        String CONFIG_MISC = "--- MISC ---";
        public boolean unlockRecipes = true;
        public boolean enableServerWarp = true;
        String CONFIG_GAMERULES = "--- GAMERULES ---";
        public boolean enableGlobalGameRules = true;
        public boolean disablePlayerMovementCheck = true;
        public boolean disableElytraMovementCheck = false;
        public boolean reducedDebugInfo = true;
        public int spawnChunkRadius = 0;
        public int playerSleepingPercentage = 100;
        String CONFIG_XP = "--- XP ---";
        public boolean disableBreedingXp = true;
        public boolean disableFishingXp = true;
        public boolean disableMiningXp = true;
        public boolean disableSmeltingXp = true;
        public boolean disableTradingXp = true;
        public boolean enableFullEnchantCost = true;
        String CONFIG_LOGGING = "--- LOGGING ---";
        public boolean loggingEnabled = true;
    }
}
