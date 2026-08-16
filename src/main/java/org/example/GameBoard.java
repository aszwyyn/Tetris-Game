package org.example;

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

public class GameBoard extends BorderPane {

    private static final int ROWS = 20;

    private static final int COLUMNS = 10;

    private static final int CELL_SIZE = 25;

    private final Color[][] board =
            new Color[ROWS][COLUMNS];

    private final Pane gridLayer =
            new Pane();

    private final Pane fixedLayer =
            new Pane();

    private final Pane pieceLayer =
            new Pane();

    private final StackPane playArea =
            new StackPane();

    private final Random random =
            new Random();

    private Tetromino currentPiece;

    private Timeline gameLoop;

    private boolean paused = false;

    private boolean gameOver = false;

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


    public GameBoard() {

        createPlayArea();

        createSidePanel();

        setupKeyboard();

        updateInformation();

        createNewPiece();

        startGameLoop();

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
                new Tetromino(type);

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

    private void moveLeft() {

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

    private void moveRight() {

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

    private void softDrop() {

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

    private void rotatePiece() {

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

    private void hardDrop() {

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

            case 1 -> 100 * level;

            case 2 -> 300 * level;

            case 3 -> 500 * level;

            default -> 800 * level;
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


    // =================================================
    // GAME LOOP
    // =================================================

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

    private void togglePause() {

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

    private void endGame() {

        gameOver = true;

        if (gameLoop != null) {
            gameLoop.stop();
        }

        statusLabel.setText(
                "GAME OVER\nPress R"
        );

        updateInformation();
    }


    // =================================================
    // RESTART
    // =================================================

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

        requestFocus();
    }


    // =================================================
    // INFORMATION
    // =================================================

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

        if (paused) {

            statusLabel.setText(
                    "PAUSED"
            );
        }
    }
}