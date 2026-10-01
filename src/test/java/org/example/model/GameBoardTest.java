package org.example.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class GameBoardTest {

    @Test
    void tetrominoCanBeCreated() {
        Tetromino piece = new Tetromino(Tetromino.Type.I);

        assertNotNull(piece);
    }
    @Test
    void tetrominoHasFourCells() {
        Tetromino piece = new Tetromino(Tetromino.Type.I);

        Position[] cells = piece.getCells();

        assertEquals(4, cells.length);
    }
    @Test
    void tetrominoCanMove() {
        Tetromino piece = new Tetromino(Tetromino.Type.I);

        int startColumn = piece.getColumn();

        piece.moveRight();

        assertEquals(startColumn + 1, piece.getColumn());
    }
    @Test
    void tetrominoCanMoveDown() {
        Tetromino piece = new Tetromino(Tetromino.Type.T);

        int startRow = piece.getRow();

        piece.moveDown();

        assertEquals(startRow + 1, piece.getRow());
    }
    @Test
    void tetrominoCanRotate() {
        Tetromino piece = new Tetromino(Tetromino.Type.I);

        Position[] before = piece.getCells();

        piece.rotate();

        Position[] after = piece.getCells();

        assertFalse(java.util.Arrays.equals(before, after));
    }
    @Test
    void tetrominoCanRotateBack() {
        Tetromino piece = new Tetromino(Tetromino.Type.I);

        Position[] original = piece.getCells();

        piece.rotate();
        piece.rotateBack();

        Position[] after = piece.getCells();

        assertArrayEquals(original, after);
    }
    @Test
    void tetrominoCanMoveLeft() {
        Tetromino piece = new Tetromino(Tetromino.Type.T);

        int startColumn = piece.getColumn();

        piece.moveLeft();

        assertEquals(startColumn - 1, piece.getColumn());
    }
    @Test
    void tetrominoReturnsCorrectType() {
        Tetromino piece = new Tetromino(Tetromino.Type.T);

        assertEquals(Tetromino.Type.T, piece.getType());
    }
    @Test
    void tetrominoCanMoveRight() {
        Tetromino piece = new Tetromino(Tetromino.Type.T);

        int startColumn = piece.getColumn();

        piece.moveRight();

        assertEquals(startColumn + 1, piece.getColumn());
    }
    @Test
    void oTetrominoDoesNotRotate() {
        Tetromino piece = new Tetromino(Tetromino.Type.O);

        Position[] before = piece.getCells();

        piece.rotate();

        Position[] after = piece.getCells();

        assertArrayEquals(before, after);
    }
    @ParameterizedTest
    @EnumSource(Tetromino.Type.class)
    void everyTetrominoHasFourCells(Tetromino.Type type) {

        Tetromino piece = new Tetromino(type);

        Position[] cells = piece.getCells();

        assertEquals(4, cells.length);
    }
}