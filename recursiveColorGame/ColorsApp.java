package com.example.demo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class  ColorsApp extends Application {

    Pane root = new Pane();
    ColorGrid colorGrid;

    @Override
    public void start(Stage stage) throws Exception {
        //1.) uloha
        int[][] grid = readGridFromFile("room4.txt");
        colorGrid = new ColorGrid(grid,root);
        //2.) uloha
        GridAnalyzer ga = new GridAnalyzer(colorGrid);
        ga.analyzeGrid();
        //3.) uloha
        int windowWidth = ColorGrid.blockSize*grid[0].length;
        int windowHeight = ColorGrid.blockSize*grid.length;
        root.setPrefSize(windowWidth, windowHeight);
        //vytvorime scenu a nastavime jej root node
        Scene scene = new Scene(root, windowWidth, windowHeight);
        //nazov okna
        stage.setTitle("farbicky");
        //priradie scenu do okna
        stage.setScene(scene);
        //zapneme to
        stage.show();
        //nakreslime si grid
        colorGrid.drawGrid();

        //4.) uloha
        root.setOnMouseClicked(event -> {
            ColorImportantArea((int)(event.getX() / ColorGrid.blockSize), (int)(event.getY() / ColorGrid.blockSize));
        });
    }

    private int[][] readGridFromFile(String filename) throws Exception {

        int[][] grid;

        //lomeno znamena hladame priamo v src/main/resources.
        InputStream input = getClass().getResourceAsStream("/" + filename);
        if (input == null) {
            throw new RuntimeException("Súbor " + filename + " sa nenašiel v resources.");
        }
        List<String> lines = new BufferedReader(
                new InputStreamReader(input)
        ).lines().toList();

        //zadefinujeme pocet riadkov do matice
        grid = new int[lines.size()][];

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);

            //informacie o kachlickach su oddelene medzerou
            String[] parts = line.split(" ");

            grid[row] = new int[parts.length];

            for (int coll = 0; coll < parts.length; coll++) {
                grid[row][coll] = Integer.parseInt(parts[coll]);
            }
        }

        return grid;
    }






    ArrayList<Vector2D> visited = new ArrayList<Vector2D>();

    void ColorImportantArea(int x, int y) {
        int newValue = colorGrid.getValue(new Vector2D(x, y));
        visited = new ArrayList<Vector2D>();

        recursiveFillBlob(new Vector2D(0,0), newValue);

        colorGrid.drawGrid();
    }

    void recursiveFillBlob(Vector2D pos, int newValue) {
        if(isVisited(pos)) return;

        int original = colorGrid.getValue(pos);
        colorGrid.setValue(pos, newValue);
        visited.add(pos);

        //left
        if (pos.x > 0) {
            if(colorGrid.getValue(pos.oneLeft()) == original)
            {
                recursiveFillBlob(pos.oneLeft(), newValue);
            }
        }
        //right
        if (pos.x < colorGrid.width()-1) {
            if(colorGrid.getValue(pos.oneRight()) == original)
            {
                recursiveFillBlob(new Vector2D(pos.x+1, pos.y), newValue);
            }
        }
        //top
        if (pos.y > 0) {
            if(colorGrid.getValue(pos.oneUp()) == original)
            {
                recursiveFillBlob(new Vector2D(pos.x, pos.y-1), newValue);
            }

        }
        //bellow
        if (pos.y < colorGrid.height()-1) {
            if(colorGrid.getValue(pos.oneDown()) == original)
            {
                recursiveFillBlob(new Vector2D(pos.x, pos.y+1), newValue);
            }
        }
    }



    boolean isVisited(Vector2D pos) {
        for(Vector2D v : visited) {
            if (v.x == pos.x && v.y == pos.y) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch();
    }
}