package org.example.view;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.GameBoard;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import java.util.Optional;
import org.example.model.HighScoreFileManager;
import org.example.model.NetworkManager;
import org.example.model.ConfigurationManager;
import javafx.scene.layout.HBox;

public class TetrisApplication
        extends Application {

    private Stage stage;

    private Scene mainMenuScene;


    @Override
    public void start(Stage stage) {

        this.stage = stage;
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.setTitle(
                "7010ICT Tetris"
        );

        stage.setResizable(false);

        showSplash();
    }


    // =============================================
    // SPLASH
    // =============================================

    private void showSplash() {

        Label title =
                new Label("TETRIS");

        title.setStyle(
                "-fx-font-size: 55px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label course =
                new Label(
                        "7010ICT\n" +
                                "Object Oriented Software Development"
                );

        course.setStyle(
                "-fx-text-fill: lightgray;" +
                        "-fx-font-size: 17px;"
        );

        course.setAlignment(
                Pos.CENTER
        );

        Label group =
                new Label(
                        "Milestone 1"
                );

        group.setStyle(
                "-fx-text-fill: white;"
        );

        VBox root =
                new VBox(
                        20,
                        title,
                        course,
                        group
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setStyle(
                "-fx-background-color: #15151f;"
        );

        Scene scene =
                new Scene(
                        root,
                        700,
                        600
                );

        stage.setScene(scene);

        stage.show();

        PauseTransition pause =
                new PauseTransition(
                        Duration.seconds(3)
                );

        pause.setOnFinished(
                event -> showMainMenu()
        );

        pause.play();
    }


    // =============================================
    // MAIN MENU
    // =============================================

    private void showMainMenu() {

        Label title =
                new Label("TETRIS");

        title.setStyle(
                "-fx-font-size: 55px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Button play =
                createMenuButton(
                        "PLAY"
                );
        Button twoPlayer =
                createMenuButton(
                        "TWO PLAYER"
                );

        Button configuration =
                createMenuButton(
                        "CONFIGURATION"
                );

        Button scores =
                createMenuButton(
                        "HIGH SCORES"
                );

        Button exit =
                createMenuButton(
                        "EXIT"
                );

        play.setOnAction(
                event -> showGame()
        );
        twoPlayer.setOnAction(
                event -> showTwoPlayerGame()
        );
        configuration.setOnAction(
                event ->
                        showConfiguration()
        );

        scores.setOnAction(
                event ->
                        showHighScores()
        );

        exit.setOnAction(
                event ->
                        confirmExit()
        );

        VBox root =
                new VBox(
                        18,
                        title,
                        play,
                        twoPlayer,
                        configuration,
                        scores,
                        exit
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setStyle(
                "-fx-background-color: #15151f;"
        );

        mainMenuScene =
                new Scene(
                        root,
                        700,
                        600
                );

        stage.setScene(
                mainMenuScene
        );
    }


    private Button createMenuButton(
            String text) {

        Button button =
                new Button(text);

        button.setPrefSize(
                220,
                45
        );

        button.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        return button;
    }


    // =============================================
    // GAME
    // =============================================

    private void showGame() {

        GameBoard game =
                new GameBoard();

        Scene scene =
                new Scene(game);

        stage.setScene(scene);

        stage.sizeToScene();

        stage.centerOnScreen();

        game.requestFocus();
    }
    private void showTwoPlayerGame() {

        long sharedSeed = System.nanoTime();

        GameBoard playerOne =
                new GameBoard(false, sharedSeed);

        GameBoard playerTwo =
                new GameBoard(true, sharedSeed);

        HBox root =
                new HBox(
                        20,
                        playerOne,
                        playerTwo
                );

        root.setAlignment(Pos.CENTER);

        root.setStyle(
                "-fx-background-color: #111111;"
        );

        Scene scene =
                new Scene(root);

        stage.setScene(scene);

        stage.sizeToScene();

        stage.centerOnScreen();

        playerOne.requestFocus();
    }


    // =============================================
    // CONFIGURATION
    // =============================================

    private void showConfiguration() {

        ConfigurationManager.GameConfig config =
                ConfigurationManager.loadConfig();

        Label title =
                new Label("CONFIGURATION");

        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label levelText =
                new Label(
                        "Starting Level: " +
                                config.startingLevel
                );

        levelText.setStyle(
                "-fx-text-fill: white;"
        );

        Slider level =
                new Slider(
                        1,
                        10,
                        config.startingLevel
                );

        level.setShowTickLabels(true);
        level.setShowTickMarks(true);
        level.setMajorTickUnit(1);
        level.setSnapToTicks(true);

        level.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        levelText.setText(
                                "Starting Level: " +
                                        newValue.intValue()
                        )
        );

        ComboBox<String> fieldSize =
                new ComboBox<>();

        fieldSize.getItems().addAll(
                "10 x 20",
                "12 x 24",
                "14 x 28"
        );

        fieldSize.setValue(
                config.fieldSize
        );

        CheckBox music =
                new CheckBox("Music");

        CheckBox sound =
                new CheckBox("Sound Effects");

        CheckBox ai =
                new CheckBox("AI Play");

        CheckBox extended =
                new CheckBox("Extended Mode");

        music.setSelected(config.music);
        sound.setSelected(config.sound);
        ai.setSelected(config.ai);
        extended.setSelected(config.extendedMode);

        music.setStyle(
                "-fx-text-fill: white;"
        );

        sound.setStyle(
                "-fx-text-fill: white;"
        );

        ai.setStyle(
                "-fx-text-fill: white;"
        );

        extended.setStyle(
                "-fx-text-fill: white;"
        );

        Button save =
                createMenuButton(
                        "SAVE CONFIGURATION"
                );

        Label savedMessage =
                new Label("");

        savedMessage.setStyle(
                "-fx-text-fill: lightgreen;" +
                        "-fx-font-weight: bold;"
        );

        save.setOnAction(event -> {

            ConfigurationManager.GameConfig newConfig =
                    new ConfigurationManager.GameConfig();

            newConfig.startingLevel =
                    (int) level.getValue();

            newConfig.fieldSize =
                    fieldSize.getValue();

            newConfig.music =
                    music.isSelected();

            newConfig.sound =
                    sound.isSelected();

            newConfig.ai =
                    ai.isSelected();

            newConfig.extendedMode =
                    extended.isSelected();

            ConfigurationManager.saveConfig(
                    newConfig
            );

            savedMessage.setText(
                    "Configuration saved!"
            );
        });

        Button back =
                createMenuButton("BACK");

        back.setOnAction(
                event ->
                        stage.setScene(
                                mainMenuScene
                        )
        );

        Label fieldSizeLabel =
                new Label("Field Size");

        fieldSizeLabel.setStyle(
                "-fx-text-fill: white;"
        );

        VBox root =
                new VBox(
                        15,
                        title,
                        fieldSizeLabel,
                        fieldSize,
                        levelText,
                        level,
                        music,
                        sound,
                        ai,
                        extended,
                        save,
                        savedMessage,
                        back
                );

        root.setPadding(
                new Insets(30)
        );

        root.setAlignment(
                Pos.CENTER
        );

        root.setStyle(
                "-fx-background-color: #15151f;"
        );

        Scene scene =
                new Scene(
                        root,
                        700,
                        700
                );

        stage.setScene(scene);
    }

    private void showHighScores() {

        Label title =
                new Label("HIGH SCORES");

        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        VBox scoresBox =
                new VBox(8);

        scoresBox.setAlignment(
                Pos.CENTER
        );

        List<HighScoreFileManager.ScoreEntry> scores =
                new java.util.ArrayList<>(
                        HighScoreFileManager.loadScores()
                );

        scores.sort(
                Comparator.comparingInt(
                        HighScoreFileManager.ScoreEntry::score
                ).reversed()
        );

        int numberOfScores =
                Math.min(10, scores.size());

        if (numberOfScores == 0) {

            Label noScores =
                    new Label("No high scores yet.");

            noScores.setStyle(
                    "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;"
            );

            scoresBox.getChildren().add(
                    noScores
            );

        } else {

            for (int i = 0;
                 i < numberOfScores;
                 i++) {

                HighScoreFileManager.ScoreEntry entry =
                        scores.get(i);

                Label scoreLabel =
                        new Label(
                                (i + 1)
                                        + ". "
                                        + entry.name()
                                        + "     "
                                        + entry.score()
                        );

                scoreLabel.setStyle(
                        "-fx-text-fill: white;" +
                                "-fx-font-size: 16px;"
                );

                scoresBox.getChildren().add(
                        scoreLabel
                );
            }
        }

        Button back =
                createMenuButton("BACK");

        back.setOnAction(
                event ->
                        stage.setScene(
                                mainMenuScene
                        )
        );

        VBox root =
                new VBox(
                        20,
                        title,
                        scoresBox,
                        back
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setStyle(
                "-fx-background-color: #15151f;"
        );

        Scene scene =
                new Scene(
                        root,
                        700,
                        650
                );

        stage.setScene(scene);
    }

    private void confirmExit() {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(
                "Exit Tetris"
        );

        alert.setHeaderText(
                "Exit the game?"
        );

        alert.setContentText(
                "Are you sure you want to exit?"
        );

        Optional<ButtonType> result =
                alert.showAndWait();

        if (result.isPresent()
                &&
                result.get()
                        == ButtonType.OK) {

            stage.close();
        }
    }


    public static void main(
            String[] args) {

        launch(args);
    }
}