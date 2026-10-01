package org.example;

import javafx.application.Platform;
import org.example.model.GameBoard;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameBoardTest {

    @BeforeAll
    static void startJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    void gameBoardCanBeCreated() {
        GameBoard board = new GameBoard();

        assertNotNull(board);
    }
    @Test
    void moveLeftCanBeCalled() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.moveLeft());
    }
    @Test
    void moveRightCanBeCalled() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.moveRight());
    }
    @Test
    void softDropCanBeCalled() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.softDrop());
    }
    @Test
    void rotatePieceCanBeCalled() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.rotatePiece());
    }
    @Test
    void hardDropCanBeCalled() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.hardDrop());
    }
    @Test
    void restartGameCanBeCalled() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.restartGame());
    }
    @ParameterizedTest
    @ValueSource(strings = {"left", "right", "down", "rotate"})
    void movementActionsCanBeCalled(String action) {

        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> {
            switch (action) {
                case "left" -> board.moveLeft();
                case "right" -> board.moveRight();
                case "down" -> board.softDrop();
                case "rotate" -> board.rotatePiece();
            }
        });
    }
    @Test
    void gameBoardSpyTest() {

        GameBoard board = spy(new GameBoard());

        board.moveLeft();

        verify(board).moveLeft();
    }
    @Test
    void gameBoardMockTest() {

        GameBoard board = mock(GameBoard.class);

        board.moveRight();

        verify(board).moveRight();
    }
}