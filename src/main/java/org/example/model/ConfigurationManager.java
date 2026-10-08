package org.example.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationManager {

    private static final Path CONFIG_FILE =
            Path.of("configuration.json");

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    public static class GameConfig {

        public int startingLevel = 1;

        public String fieldSize =
                "10 x 20";

        public boolean music = true;

        public boolean sound = true;

        public boolean ai = false;

        public boolean extendedMode = false;
    }

    public static void saveConfig(
            GameConfig config) {

        try {

            String json =
                    GSON.toJson(config);

            Files.writeString(
                    CONFIG_FILE,
                    json
            );

            System.out.println(
                    "Configuration saved successfully."
            );

        } catch (IOException e) {

            System.out.println(
                    "Could not save configuration: "
                            + e.getMessage()
            );
        }
    }

    public static GameConfig loadConfig() {

        if (!Files.exists(CONFIG_FILE)) {

            return new GameConfig();
        }

        try {

            String json =
                    Files.readString(
                            CONFIG_FILE
                    );

            GameConfig config =
                    GSON.fromJson(
                            json,
                            GameConfig.class
                    );

            if (config != null) {

                return config;
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not load configuration: "
                            + e.getMessage()
            );
        }

        return new GameConfig();
    }
}