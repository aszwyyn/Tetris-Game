package org.example.controller;

import org.example.model.GameBoard;

public class GameController {

    private final GameBoard gameBoard;

    public GameController(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void moveLeft() {
        gameBoard.moveLeft();
    }
    public void moveRight() {
        gameBoard.moveRight();
    }
    public void softDrop() {
        gameBoard.softDrop();
    }
    public void rotatePiece() {
        gameBoard.rotatePiece();
    }
    public void hardDrop() {
        gameBoard.hardDrop();
    }
    public void restartGame() {
        gameBoard.restartGame();

    }
    public void togglePause () {
        gameBoard.togglePause();
    }

}