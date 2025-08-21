package net.miron.playervaults;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ConfigManager {
    private static final File configFile = new File("config/playervaults.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static VaultsConfig config = new VaultsConfig();

    public static void load() {
        try {
            if (!configFile.exists()) {
                save();
            } else {
                FileReader reader = new FileReader(configFile);
                config = gson.fromJson(reader, VaultsConfig.class);
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            configFile.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(config, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
