package com.example.demo;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorGrid {

    private int[][] grid; //[row][coll]
    Pane root = new Pane();
    public static final int blockSize = 30;

    public ColorGrid(int[][] grid, Pane root) {
        this.grid = grid;
        this.root = root;
    }

    public int height() { return grid.length;}
    public int width() { return grid[0].length;}

    void drawGrid() {
        for(int y = 0; y < grid.length;y++) {
            for (int x = 0; x < grid[0].length; x++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setX(x*blockSize);
                rectangle.setY(y*blockSize);
                rectangle.setWidth(blockSize);
                rectangle.setHeight(blockSize);
                rectangle.setFill(int2Color(grid[y][x]));
                root.getChildren().add(rectangle);
            }
        }
    }

    public int getValue(Vector2D pos) {
        return grid[pos.y][pos.x];
    }

    public void setValue(Vector2D pos, int value) {
        grid[pos.y][pos.x] = value;
    }

    public Color getColor(Vector2D pos) {
        return int2Color(grid[pos.y][pos.x]);
    }

    static Color int2Color(int num) {
        switch(num) {
            case 0:
                return Color.YELLOW;
            case 1:
                return Color.PINK;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BROWN;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.ORANGE;
            case 6:
                return Color.BLUE;
            case 7:
                return Color.VIOLET;
            case 8:
                return Color.PURPLE;
            case 9:
                return Color.MAGENTA;
        }
        return Color.BLACK;
    };
}
