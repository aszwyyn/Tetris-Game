package org.example.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;



public class GameBoard extends BorderPane {

    private final int ROWS;

    private final int COLUMNS;

    private static final int CELL_SIZE = 25;

    private final Color[][] board;

    private final Pane gridLayer =
            new Pane();

    private final Pane fixedLayer =
            new Pane();

    private final Pane pieceLayer =
            new Pane();

    private final StackPane playArea =
            new StackPane();

    private final Random random;


    private Tetromino currentPiece;

    private Timeline gameLoop;

    private boolean paused = false;

    private boolean gameOver = false;
    private boolean musicEnabled;

    private boolean soundEnabled;
    private MediaPlayer musicPlayer;
    private AudioClip clearSound;
    private boolean aiEnabled;
    private Timeline aiLoop;

    private boolean animationRunning = false;

    private int score = 0;

    private int lines = 0;

    private int level = 1;

    private final Label scoreLabel =
            new Label();

    private final Label linesLabel =
            new Label();

    private final Label levelLabel =
            new Label();

    private final Label statusLabel =
            new Label();
    private final Label playerTypeLabel =
            new Label();


    public GameBoard() {
        this(null, System.nanoTime());
    }

    public GameBoard(Boolean aiOverride, long seed) {

        random = new Random(seed);

        ConfigurationManager.GameConfig config =
                ConfigurationManager.loadConfig();
        musicEnabled = config.music;
        soundEnabled = config.sound;
        aiEnabled =
                aiOverride != null
                        ? aiOverride
                        : config.ai;
        try {
            String musicPath = getClass()
                    .getResource("/audio/music.mp3")
                    .toExternalForm();

            Media music = new Media(musicPath);
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(0.35);

            String soundPath = getClass()
                    .getResource("/audio/clear.mp3")
                    .toExternalForm();

            clearSound = new AudioClip(soundPath);

            if (musicEnabled) {
                musicPlayer.play();
            }

        } catch (Exception e) {
            System.out.println("Audio loading error: " + e.getMessage());
        }

        String size = config.fieldSize;

        if ("12 x 24".equals(size)) {
            COLUMNS = 12;
            ROWS = 24;

        } else if ("14 x 28".equals(size)) {
            COLUMNS = 14;
            ROWS = 28;

        } else {
            COLUMNS = 10;
            ROWS = 20;
        }

        board = new Color[ROWS][COLUMNS];

        createPlayArea();

        createSidePanel();

        setupKeyboard();

        updateInformation();

        createNewPiece();

        startGameLoop();

        if (aiEnabled) {
            startAI();
        }

        setStyle(
                "-fx-background-color: #111111;"
        );

        setFocusTraversable(true);

        requestFocus();
    }


    // =================================================
    // PLAY AREA
    // =================================================

    private void createPlayArea() {

        double width =
                COLUMNS * CELL_SIZE;

        double height =
                ROWS * CELL_SIZE;

        gridLayer.setPrefSize(
                width,
                height
        );

        fixedLayer.setPrefSize(
                width,
                height
        );

        pieceLayer.setPrefSize(
                width,
                height
        );

        for (int row = 0; row < ROWS; row++) {

            for (int column = 0;
                 column < COLUMNS;
                 column++) {

                Rectangle cell =
                        new Rectangle(
                                CELL_SIZE,
                                CELL_SIZE
                        );

                cell.setFill(
                        Color.rgb(15, 15, 20)
                );

                cell.setStroke(
                        Color.rgb(55, 55, 65)
                );

                cell.setTranslateX(
                        column * CELL_SIZE
                );

                cell.setTranslateY(
                        row * CELL_SIZE
                );

                gridLayer.getChildren()
                        .add(cell);
            }
        }

        playArea.getChildren().addAll(
                gridLayer,
                fixedLayer,
                pieceLayer
        );

        playArea.setMinSize(
                width,
                height
        );

        playArea.setMaxSize(
                width,
                height
        );

        playArea.setStyle(
                "-fx-border-color: white;" +
                        "-fx-border-width: 2px;"
        );

        setCenter(playArea);

        BorderPane.setMargin(
                playArea,
                new Insets(20)
        );
    }


    // =================================================
    // SIDE PANEL
    // =================================================

    private void createSidePanel() {

        Label title =
                new Label("TETRIS");

        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        styleInformationLabel(scoreLabel);
        styleInformationLabel(linesLabel);
        styleInformationLabel(levelLabel);
        styleInformationLabel(playerTypeLabel);

        statusLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: gold;"
        );

        Label controls =
                new Label(
                        """
                        CONTROLS
        
                        ←  Move Left
                        →  Move Right
                        ↓  Soft Drop
                        ↑  Rotate
                        SPACE  Hard Drop
                        P  Pause
                        R  Restart
                        M  Music On/Off
                        S  Sound On/Off
                        """
                );

        controls.setStyle(
                "-fx-text-fill: lightgray;" +
                        "-fx-font-size: 13px;"
        );

        Button restart =
                new Button("Restart");

        restart.setPrefWidth(130);

        restart.setOnAction(
                event -> restartGame()
        );

        VBox side =
                new VBox(
                        15,
                        title,
                        scoreLabel,
                        linesLabel,
                        levelLabel,
                        playerTypeLabel,
                        statusLabel,
                        controls,
                        restart
                );

        side.setAlignment(
                Pos.TOP_CENTER
        );

        side.setPadding(
                new Insets(25)
        );

        side.setPrefWidth(200);

        side.setStyle(
                "-fx-background-color: #202028;"
        );

        setRight(side);
    }


    private void styleInformationLabel(
            Label label) {

        label.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );
    }


    // =================================================
    // KEYBOARD
    // =================================================

    private void setupKeyboard() {

        setOnKeyPressed(event -> {

            if (event.getCode()
                    == KeyCode.R) {

                restartGame();

                return;
            }

            if (event.getCode()
                    == KeyCode.P) {

                togglePause();

                return;
            }
            if (event.getCode() == KeyCode.M) {

                musicEnabled = !musicEnabled;

                statusLabel.setText(
                        musicEnabled
                                ? "MUSIC ON"
                                : "MUSIC OFF"
                );

                return;
            }

            if (event.getCode() == KeyCode.S) {

                soundEnabled = !soundEnabled;

                statusLabel.setText(
                        soundEnabled
                                ? "SOUND ON"
                                : "SOUND OFF"
                );

                return;
            }
            if (paused ||
                    gameOver ||
                    animationRunning ||
                    currentPiece == null) {

                return;
            }

            switch (event.getCode()) {

                case LEFT -> moveLeft();

                case RIGHT -> moveRight();

                case DOWN -> softDrop();

                case UP -> rotatePiece();

                case SPACE -> hardDrop();

                default -> {
                }
            }
        });
    }


    // =================================================
    // NEW PIECE
    // =================================================

    private void createNewPiece() {

        Tetromino.Type[] types =
                Tetromino.Type.values();

        Tetromino.Type type =
                types[
                        random.nextInt(
                                types.length
                        )
                        ];

        currentPiece =
                TetrominoFactory.createTetromino(type);

        currentPiece.setGridPosition(
                3,
                0
        );

        pieceLayer.getChildren()
                .add(currentPiece);

        if (!canPlace(
                currentPiece,
                currentPiece.getColumn(),
                currentPiece.getRow())) {

            endGame();
        }
    }


    // =================================================
    // LEFT
    // =================================================

    public void moveLeft() {

        int newColumn =
                currentPiece.getColumn() - 1;

        if (canPlace(
                currentPiece,
                newColumn,
                currentPiece.getRow())) {

            currentPiece.setGridPosition(
                    newColumn,
                    currentPiece.getRow()
            );
        }
    }


    // =================================================
    // RIGHT
    // =================================================

    public void moveRight() {

        int newColumn =
                currentPiece.getColumn() + 1;

        if (canPlace(
                currentPiece,
                newColumn,
                currentPiece.getRow())) {

            currentPiece.setGridPosition(
                    newColumn,
                    currentPiece.getRow()
            );
        }
    }


    // =================================================
    // SOFT DROP
    // =================================================

    public void softDrop() {

        int newRow =
                currentPiece.getRow() + 1;

        if (canPlace(
                currentPiece,
                currentPiece.getColumn(),
                newRow)) {

            currentPiece.setGridPosition(
                    currentPiece.getColumn(),
                    newRow
            );

            score++;

            updateInformation();

        } else {

            lockPiece();
        }
    }


    // =================================================
    // SMOOTH AUTOMATIC FALL
    // =================================================

    private void automaticDrop() {

        if (paused ||
                gameOver ||
                animationRunning ||
                currentPiece == null) {

            return;
        }

        int targetRow =
                currentPiece.getRow() + 1;

        if (!canPlace(
                currentPiece,
                currentPiece.getColumn(),
                targetRow)) {

            lockPiece();

            return;
        }

        animationRunning = true;

        TranslateTransition transition =
                new TranslateTransition(
                        Duration.millis(180),
                        currentPiece
                );

        transition.setFromY(
                currentPiece.getTranslateY()
        );

        transition.setToY(
                targetRow * CELL_SIZE
        );

        transition.setOnFinished(event -> {

            currentPiece.setGridPosition(
                    currentPiece.getColumn(),
                    targetRow
            );

            animationRunning = false;
        });

        transition.play();
    }


    // =================================================
    // ROTATION
    // =================================================

    public void rotatePiece() {

        currentPiece.rotate();

        if (canPlace(
                currentPiece,
                currentPiece.getColumn(),
                currentPiece.getRow())) {

            return;
        }

        // Basic wall kick left
        if (canPlace(
                currentPiece,
                currentPiece.getColumn() - 1,
                currentPiece.getRow())) {

            currentPiece.setGridPosition(
                    currentPiece.getColumn() - 1,
                    currentPiece.getRow()
            );

            return;
        }

        // Basic wall kick right
        if (canPlace(
                currentPiece,
                currentPiece.getColumn() + 1,
                currentPiece.getRow())) {

            currentPiece.setGridPosition(
                    currentPiece.getColumn() + 1,
                    currentPiece.getRow()
            );

            return;
        }

        currentPiece.rotateBack();
    }


    // =================================================
    // HARD DROP
    // =================================================

    public void hardDrop() {

        int distance = 0;

        while (canPlace(
                currentPiece,
                currentPiece.getColumn(),
                currentPiece.getRow() + 1)) {

            currentPiece.setGridPosition(
                    currentPiece.getColumn(),
                    currentPiece.getRow() + 1
            );

            distance++;
        }

        score += distance * 2;

        lockPiece();
    }


    // =================================================
    // COLLISION
    // =================================================

    private boolean canPlace(
            Tetromino piece,
            int baseColumn,
            int baseRow) {

        for (Position cell :
                piece.getCells()) {

            int column =
                    baseColumn +
                            cell.column();

            int row =
                    baseRow +
                            cell.row();

            if (column < 0 ||
                    column >= COLUMNS) {

                return false;
            }

            if (row < 0 ||
                    row >= ROWS) {

                return false;
            }

            if (board[row][column]
                    != null) {

                return false;
            }
        }

        return true;
    }


    // =================================================
    // LOCK PIECE
    // =================================================
    /**
     * Locks the current Tetromino into the game board.
     * Stores each occupied cell and then checks for completed lines.
     */
    private void lockPiece() {

        if (currentPiece == null) {
            return;
        }

        Color color =
                currentPiece.getColor();

        for (Position cell :
                currentPiece.getCells()) {

            int column =
                    currentPiece.getColumn()
                            + cell.column();

            int row =
                    currentPiece.getRow()
                            + cell.row();

            if (row >= 0 &&
                    row < ROWS &&
                    column >= 0 &&
                    column < COLUMNS) {

                board[row][column] =
                        color;
            }
        }

        pieceLayer.getChildren()
                .remove(currentPiece);

        currentPiece = null;

        score += 10;

        clearLines();

        drawFixedBlocks();

        updateInformation();

        createNewPiece();
    }


    // =================================================
    // LINE CLEARING
    // =================================================
    /**
     * Checks the game board for completed rows.
     * Removes full lines and updates the player's score.
     */
    private void clearLines() {

        int cleared = 0;

        for (int row = ROWS - 1;
             row >= 0;
             row--) {

            boolean full = true;

            for (int column = 0;
                 column < COLUMNS;
                 column++) {

                if (board[row][column]
                        == null) {

                    full = false;

                    break;
                }
            }

            if (full) {

                cleared++;

                for (int moveRow = row;
                     moveRow > 0;
                     moveRow--) {

                    for (int column = 0;
                         column < COLUMNS;
                         column++) {

                        board[moveRow][column] =
                                board[
                                        moveRow - 1
                                        ][column];
                    }
                }

                for (int column = 0;
                     column < COLUMNS;
                     column++) {

                    board[0][column] =
                            null;
                }

                // Recheck same row
                row++;
            }
        }

        if (cleared == 0) {
            return;
        }

        lines += cleared;

        score += switch (cleared) {

            case 1 -> 100;

            case 2 -> 300;

            case 3 -> 600;

            case 4 -> 1000;

            default -> 0;
        };

        int newLevel =
                (lines / 10) + 1;

        if (newLevel != level) {

            level = newLevel;

            startGameLoop();
        }
    }


    // =================================================
    // DRAW FIXED BLOCKS
    // =================================================
    /**
     * Redraws all fixed blocks currently stored on the game board.
     * Updates the visual layer to reflect the current board state.
     */
    private void drawFixedBlocks() {

        fixedLayer.getChildren()
                .clear();

        for (int row = 0;
             row < ROWS;
             row++) {

            for (int column = 0;
                 column < COLUMNS;
                 column++) {

                Color color =
                        board[row][column];

                if (color == null) {
                    continue;
                }

                Rectangle block =
                        new Rectangle(
                                CELL_SIZE,
                                CELL_SIZE
                        );

                block.setFill(color);

                block.setStroke(
                        Color.BLACK
                );

                block.setTranslateX(
                        column * CELL_SIZE
                );

                block.setTranslateY(
                        row * CELL_SIZE
                );

                fixedLayer.getChildren()
                        .add(block);
            }
        }
    }
    private void startAI() {

        if (aiLoop != null) {
            aiLoop.stop();
        }

        aiLoop = new Timeline(
                new KeyFrame(
                        Duration.millis(180),
                        event -> {

                            if (paused ||
                                    gameOver ||
                                    animationRunning ||
                                    currentPiece == null) {
                                return;
                            }

                            int bestColumn = findBestAIColumn();

                            if (currentPiece.getColumn() < bestColumn) {
                                moveRight();

                            } else if (currentPiece.getColumn() > bestColumn) {
                                moveLeft();

                            } else {
                                hardDrop();
                            }
                        }
                )
        );

        aiLoop.setCycleCount(Timeline.INDEFINITE);
        aiLoop.play();
    }


    private int findBestAIColumn() {

        int bestColumn = currentPiece.getColumn();
        int bestRow = -1;

        for (int column = -4;
             column < COLUMNS;
             column++) {

            int row = currentPiece.getRow();

            if (!canPlace(
                    currentPiece,
                    column,
                    row)) {
                continue;
            }

            while (canPlace(
                    currentPiece,
                    column,
                    row + 1)) {
                row++;
            }

            if (row > bestRow) {
                bestRow = row;
                bestColumn = column;
            }
        }

        return bestColumn;
    }
    // =================================================
    // GAME LOOP
    // =================================================
    /**
     * Starts or restarts the main game loop.
     * Controls the automatic downward movement speed based on the current level.
     */
    private void startGameLoop() {

        if (gameLoop != null) {
            gameLoop.stop();
        }

        double delay =
                Math.max(
                        150,
                        700 -
                                ((level - 1) * 60)
                );

        gameLoop =
                new Timeline(
                        new KeyFrame(
                                Duration.millis(delay),
                                event ->
                                        automaticDrop()
                        )
                );

        gameLoop.setCycleCount(
                Timeline.INDEFINITE
        );

        if (!paused &&
                !gameOver) {

            gameLoop.play();
        }
    }


    // =================================================
    // PAUSE
    // =================================================
    /**
     * Toggles the game between paused and running states.
     * Pauses or resumes the game loop and updates the status display.
     */
    public void togglePause() {

        if (gameOver) {
            return;
        }

        paused = !paused;

        if (paused) {

            gameLoop.pause();

            statusLabel.setText(
                    "PAUSED"
            );

        } else {

            gameLoop.play();

            statusLabel.setText("");
        }

        updateInformation();
    }


    // =================================================
    // GAME OVER
    // =================================================
    /**
     * Ends the current game when the player can no longer continue.
     * Stops the game loop and updates the game-over state.
     */
    private void endGame() {

        gameOver = true;

        if (gameLoop != null) {
            gameLoop.stop();
        }
        if (aiLoop != null) {
            aiLoop.stop();
        }

        statusLabel.setText(
                "GAME OVER\nPress R"
        );

        updateInformation();
    }


    // =================================================
    // RESTART
    // =================================================
    /**
     * Restarts the game and resets the board to its initial state.
     * Clears existing blocks, resets game values, and starts a new game.
     */
    public void restartGame() {

        if (gameLoop != null) {
            gameLoop.stop();
        }

        for (int row = 0;
             row < ROWS;
             row++) {

            for (int column = 0;
                 column < COLUMNS;
                 column++) {

                board[row][column] =
                        null;
            }
        }

        fixedLayer.getChildren()
                .clear();

        pieceLayer.getChildren()
                .clear();

        currentPiece = null;

        score = 0;
        lines = 0;
        level = 1;

        paused = false;
        gameOver = false;
        animationRunning = false;

        statusLabel.setText("");

        updateInformation();

        createNewPiece();

        startGameLoop();

        if (aiEnabled) {
            startAI();
        }

        requestFocus();
    }






    // =================================================
    // INFORMATION
    // =================================================
    /**
     * Updates the information displayed to the player.
     * Refreshes the score, lines, level, and current game status.
     */
    private void updateInformation() {

        scoreLabel.setText(
                "Score: " + score
        );

        linesLabel.setText(
                "Lines: " + lines
        );

        levelLabel.setText(
                "Level: " + level
        );
        playerTypeLabel.setText(
                aiEnabled
                        ? "Player: AI"
                        : "Player: Human"
        );
        if (paused) {

            statusLabel.setText(
                    "PAUSED"
            );
        }
    }
}