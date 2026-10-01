package org.example.model;

public class TetrominoFactory {

    public static Tetromino createTetromino(Tetromino.Type type) {
        return new Tetromino(type);
    }
}
