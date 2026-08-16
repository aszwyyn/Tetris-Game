package org.example;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

public class TetrisApplication
        extends Application {

    private Stage stage;

    private Scene mainMenuScene;


    @Override
    public void start(Stage stage) {

        this.stage = stage;

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

        stage.centerOnScreen();

        game.requestFocus();
    }


    // =============================================
    // CONFIGURATION
    // =============================================

    private void showConfiguration() {

        Label title =
                new Label(
                        "CONFIGURATION"
                );

        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        Label levelText =
                new Label(
                        "Starting Level: 1"
                );

        levelText.setStyle(
                "-fx-text-fill: white;"
        );

        Slider level =
                new Slider(
                        1,
                        10,
                        1
                );

        level.setShowTickLabels(true);
        level.setShowTickMarks(true);
        level.setMajorTickUnit(1);
        level.setSnapToTicks(true);

        level.valueProperty()
                .addListener(
                        (observable,
                         oldValue,
                         newValue) ->

                                levelText.setText(
                                        "Starting Level: "
                                                + newValue.intValue()
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
                "10 x 20"
        );

        CheckBox music =
                new CheckBox(
                        "Music"
                );

        CheckBox sound =
                new CheckBox(
                        "Sound Effects"
                );

        CheckBox ai =
                new CheckBox(
                        "AI Play"
                );

        CheckBox extended =
                new CheckBox(
                        "Extended Mode"
                );

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

        Button back =
                createMenuButton(
                        "BACK"
                );

        back.setOnAction(
                event ->
                        stage.setScene(
                                mainMenuScene
                        )
        );

        VBox root =
                new VBox(
                        15,
                        title,
                        new Label("Field Size"),
                        fieldSize,
                        levelText,
                        level,
                        music,
                        sound,
                        ai,
                        extended,
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
                        650
                );

        stage.setScene(scene);
    }


    // =============================================
    // HIGH SCORES
    // =============================================

    private void showHighScores() {

        Label title =
                new Label(
                        "HIGH SCORES"
                );

        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        VBox scores =
                new VBox(8);

        scores.setAlignment(
                Pos.CENTER
        );

        String[] names = {
                "Alex",
                "Sam",
                "Jordan",
                "Taylor",
                "Morgan",
                "Jamie",
                "Chris",
                "Casey",
                "Riley",
                "Dylan"
        };

        int value = 10000;

        for (int i = 0;
             i < names.length;
             i++) {

            Label score =
                    new Label(
                            (i + 1)
                                    + ". "
                                    + names[i]
                                    + "     "
                                    + value
                    );

            score.setStyle(
                    "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;"
            );

            scores.getChildren()
                    .add(score);

            value -= 750;
        }

        Button back =
                createMenuButton(
                        "BACK"
                );

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
                        scores,
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


    // =============================================
    // EXIT CONFIRMATION
    // =============================================

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