package org.example.controller;

import org.example.model.GameBoard;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GameControllerTest {

    @Test
    void moveLeftCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.moveLeft();

        verify(mockBoard).moveLeft();
    }
    @Test
    void moveRightCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.moveRight();

        verify(mockBoard).moveRight();
    }
    @Test
    void rotatePieceCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.rotatePiece();

        verify(mockBoard).rotatePiece();
    }
    @Test
    void softDropCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.softDrop();

        verify(mockBoard).softDrop();
    }

    @Test
    void hardDropCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.hardDrop();

        verify(mockBoard).hardDrop();
    }
    @Test
    void restartGameCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.restartGame();

        verify(mockBoard).restartGame();
    }
    @Test
    void togglePauseCallsGameBoard() {

        GameBoard mockBoard = mock(GameBoard.class);

        GameController controller =
                new GameController(mockBoard);

        controller.togglePause();

        verify(mockBoard).togglePause();
    }
}