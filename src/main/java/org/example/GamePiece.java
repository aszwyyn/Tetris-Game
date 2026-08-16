package org.example;

import javafx.scene.Group;

public abstract class GamePiece extends Group implements Movable {

    public static final int CELL_SIZE = 25;

    protected int column;
    protected int row;

    public GamePiece(int column, int row) {
        setGridPosition(column, row);
    }

    public void setGridPosition(int column, int row) {

        this.column = column;
        this.row = row;

        setTranslateX(column * CELL_SIZE);
        setTranslateY(row * CELL_SIZE);
    }

    @Override
    public void moveLeft() {
        setGridPosition(column - 1, row);
    }

    @Override
    public void moveRight() {
        setGridPosition(column + 1, row);
    }

    @Override
    public void moveDown() {
        setGridPosition(column, row + 1);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public abstract Position[] getCells();
}
