package org.example;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tetromino extends GamePiece {

    public enum Type {
        I, O, T, S, Z, J, L
    }

    private final Type type;

    private int rotation = 0;

    private static final int[][][][] SHAPES = {

            // I
            {
                    {{0,1},{1,1},{2,1},{3,1}},
                    {{2,0},{2,1},{2,2},{2,3}},
                    {{0,2},{1,2},{2,2},{3,2}},
                    {{1,0},{1,1},{1,2},{1,3}}
            },

            // O
            {
                    {{1,0},{2,0},{1,1},{2,1}},
                    {{1,0},{2,0},{1,1},{2,1}},
                    {{1,0},{2,0},{1,1},{2,1}},
                    {{1,0},{2,0},{1,1},{2,1}}
            },

            // T
            {
                    {{1,0},{0,1},{1,1},{2,1}},
                    {{1,0},{1,1},{2,1},{1,2}},
                    {{0,1},{1,1},{2,1},{1,2}},
                    {{1,0},{0,1},{1,1},{1,2}}
            },

            // S
            {
                    {{1,0},{2,0},{0,1},{1,1}},
                    {{1,0},{1,1},{2,1},{2,2}},
                    {{1,1},{2,1},{0,2},{1,2}},
                    {{0,0},{0,1},{1,1},{1,2}}
            },

            // Z
            {
                    {{0,0},{1,0},{1,1},{2,1}},
                    {{2,0},{1,1},{2,1},{1,2}},
                    {{0,1},{1,1},{1,2},{2,2}},
                    {{1,0},{0,1},{1,1},{0,2}}
            },

            // J
            {
                    {{0,0},{0,1},{1,1},{2,1}},
                    {{1,0},{2,0},{1,1},{1,2}},
                    {{0,1},{1,1},{2,1},{2,2}},
                    {{1,0},{1,1},{0,2},{1,2}}
            },

            // L
            {
                    {{2,0},{0,1},{1,1},{2,1}},
                    {{1,0},{1,1},{1,2},{2,2}},
                    {{0,1},{1,1},{2,1},{0,2}},
                    {{0,0},{1,0},{1,1},{1,2}}
            }
    };

    public Tetromino(Type type) {

        super(3, 0);

        this.type = type;

        draw();
    }

    private void draw() {

        getChildren().clear();

        Color color = getColor();

        for (Position position : getCells()) {

            Rectangle block =
                    new Rectangle(
                            CELL_SIZE,
                            CELL_SIZE
                    );

            block.setFill(color);
            block.setStroke(Color.BLACK);

            block.setTranslateX(
                    position.column() * CELL_SIZE
            );

            block.setTranslateY(
                    position.row() * CELL_SIZE
            );

            getChildren().add(block);
        }
    }

    @Override
    public Position[] getCells() {

        int[][] shape =
                SHAPES[type.ordinal()][rotation];

        Position[] positions =
                new Position[shape.length];

        for (int i = 0; i < shape.length; i++) {

            positions[i] =
                    new Position(
                            shape[i][0],
                            shape[i][1]
                    );
        }

        return positions;
    }

    public Color getColor() {

        return switch (type) {

            case I -> Color.CYAN;

            case O -> Color.GOLD;

            case T -> Color.MEDIUMPURPLE;

            case S -> Color.LIMEGREEN;

            case Z -> Color.RED;

            case J -> Color.DODGERBLUE;

            case L -> Color.ORANGE;
        };
    }

    @Override
    public void rotate() {

        if (type == Type.O) {
            return;
        }

        rotation =
                (rotation + 1) % 4;

        draw();
    }

    public void rotateBack() {

        if (type == Type.O) {
            return;
        }

        rotation =
                (rotation + 3) % 4;

        draw();
    }

    public Type getType() {
        return type;
    }
}