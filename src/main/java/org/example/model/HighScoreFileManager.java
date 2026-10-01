package org.example.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HighScoreFileManager {

    private static final Path SCORE_FILE = Path.of("highscores.json");

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    public record ScoreEntry(String name, int score) {
    }

    public static void saveScore(String name, int score) {

        List<ScoreEntry> scores =
                new ArrayList<>(loadScores());

        scores.add(
                new ScoreEntry(name, score)
        );

        String json =
                GSON.toJson(scores);

        try {
            Files.writeString(
                    SCORE_FILE,
                    json
            );
        } catch (IOException e) {
            System.out.println(
                    "Could not save score: "
                            + e.getMessage()
            );
        }
    }

    public static List<ScoreEntry> loadScores() {

        if (!Files.exists(SCORE_FILE)) {
            return List.of();
        }

        try {
            String json =
                    Files.readString(SCORE_FILE);

            List<ScoreEntry> scores =
                    GSON.fromJson(
                            json,
                            new TypeToken<List<ScoreEntry>>() {
                            }.getType()
                    );

            if (scores != null) {
                return scores;
            }

        } catch (IOException e) {
            System.out.println(
                    "Could not load scores: "
                            + e.getMessage()
            );
        }

        return List.of();
    }
}