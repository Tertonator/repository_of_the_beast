package com.particlesdu.demo2;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
/*
components hierarchy:
Stage: 			-jeden stage moze zobrazovat v danom case jednu scenu a sceny sa mozu prepinat
  |___Scene:	 	-scena fyzicky zobrazuje samotne komponenty
       |_____SceneGraph -stromova struktura vsetkych vizualnych prvko
			-ROOT NODE napr layout v ktorom su ostatne prvky
			-BRANCH NODES, prvky ktore mozu mat dalsie dalsie podradene prvky
			-LEAF NODES - koncove prvky ktore uz nemaju potomkov ako button alebo text
			-LAYOUT PANE(s) - tiez node ktory sluzi na organizaciu nodes pod nim
				-VBox organizuje veci pod sebou vertikalne
				-GridPane organizuje mriezku
				-HBox organizuje veci vedla seba horizontalne
				-BorderPane organizuje veci po okrajoch obrazovky
				-99percentach sa ako ROOT NODE pouzije nejaky layout pane
				-existuje vela panes na rozne ucely


BorderPane
│
├── Top: HBox
│      ├── Label("Čas")
│      └── Label("Presuny")
│
├── Center: GridPane
│      ├── Pane
│      ├── Pane
│      ├── ...
│
└── Bottom: HBox
       ├── Button("Save")
       ├── Button("Load")
       ├── Button("Prev")
       └── Button("Next")
*/

public class Main extends Application {
    int tileSize = 60;
    int edgeSize = 15;

    BorderPane rootBorderPane = new BorderPane();
    GridPane gridPane = new GridPane();
    Label infoLabel;

    int selectedBlockID = -1;

    GameState state;
    Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Scene scene = new Scene(rootBorderPane);
        stage.setTitle("fejkovy tetris");
        stage.setScene(scene);
        rootBorderPane.setFocusTraversable(true);
        rootBorderPane.requestFocus();
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> rootBorderPane.requestFocus());

        //sipky
        rootBorderPane.setOnKeyPressed(e -> {
            if (selectedBlockID == -1) return;
            int dx = 0, dy = 0;
            switch (e.getCode()) {
                case LEFT -> dx = -1;
                case RIGHT -> dx = 1;
                case UP -> dy = -1;
                case DOWN -> dy = 1;
                default -> { return; }
            }
            moveBlock(selectedBlockID, dx, dy);
            redrawGrid();
        });

        HBox infoBox = new HBox(20);
        infoLabel = new Label();
        infoBox.getChildren().add(infoLabel);

        HBox buttonBox = new HBox(20);
        Button save = new Button("Save");
        save.setOnAction(e -> state.save("savegame.txt"));

        Button load = new Button("Load");
        load.setOnAction(e -> {
            GameState loaded = GameState.load("savegame.txt");
            if (loaded != null) loadGame(loaded);
        });

        Button next = new Button("Next");
        next.setOnAction(e -> {
            if (state.currentLvl < 6) loadGame(new GameState(0, 0, state.currentLvl + 1));
        });

        Button prev = new Button("Prev");
        prev.setOnAction(e -> {
            if (state.currentLvl > 1) loadGame(new GameState(0, 0, state.currentLvl - 1));
        });

        buttonBox.getChildren().addAll(save, load, prev, next);

        rootBorderPane.setTop(infoBox);
        rootBorderPane.setBottom(buttonBox);

        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    state.elapsedTime++;
                    updateLabel();
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        stage.show();
        loadGame(new GameState(0, 0, 1));
    }

    void loadGame(GameState newState) {
        state = newState;
        selectedBlockID = -1; // reset vyberu

        int innerCols = state.grid[0].length - 2;
        int innerRows = state.grid.length - 2;
        stage.setWidth(innerCols * tileSize + 2 * edgeSize);
        stage.setHeight(innerRows * tileSize + 2 * edgeSize);

        gridPane = buildGrid();
        rootBorderPane.setCenter(gridPane);
        updateLabel();
    }


    public static void main(String[] args) {
        launch();
    }

    private GridPane buildGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int rows = state.grid.length;
        int cols = state.grid[0].length;

        for (int col = 0; col < cols; col++) {
            ColumnConstraints cc = new ColumnConstraints();
            if (col == 0 || col == cols - 1) {
                cc.setPrefWidth(edgeSize);
                cc.setHgrow(Priority.NEVER);  // okraj sa neroztiahne
            } else {
                cc.setHgrow(Priority.ALWAYS);  // vnutorne bunky sa roztiahnu
            }
            gridPane.getColumnConstraints().add(cc);
        }

        for (int row = 0; row < rows; row++) {
            RowConstraints rc = new RowConstraints();
            if (row == 0 || row == rows - 1) {
                rc.setPrefHeight(edgeSize);
                rc.setVgrow(Priority.NEVER);
            } else {
                rc.setVgrow(Priority.ALWAYS);
            }
            gridPane.getRowConstraints().add(rc);
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Pane cell = new Pane();
                cell.prefHeightProperty().bind(cell.widthProperty());

                //klikanie na cell
                final int r = row;
                final int c = col;
                cell.setOnMouseClicked(e -> {
                    int blockId = state.grid[r][c];
                    if (blockId > 0) {
                        selectedBlockID = blockId;
                    }
                });

                //nafarbenie bunky
                ColorCell(cell, row, col);

                gridPane.add(cell, col, row);
            }
        }

        return gridPane;
    }

    String[] colors = {
            "green",
            "blue",
            "yellow",
            "red",
            "purple",
            "orange",
            "cyan",
            "pink",
            "brown",
            "grey",
            "lightblue",
            "lightgreen"
    };


    private void moveBlock(int blockId, int dx, int dy) {
        // najdi vsetky bunky bloku
        List<Vector2D> cells = new ArrayList<>();
        for (int r = 1; r < state.grid.length - 1; r++) {
            for (int c = 1; c < state.grid[0].length - 1; c++) {
                if (state.grid[r][c] == blockId) {
                    cells.add(new Vector2D(c, r));
                }
            }
        }

        // skontroluj ci moze pohnut
        for (Vector2D cell : cells) {
            Vector2D newPos = new Vector2D(cell.col + dx, cell.row + dy);
            int target = state.grid[newPos.row][newPos.col];
            if (target != 0 && target != blockId) {
                return;
            }
        }

        // vymaz stare pozicie
        for (Vector2D cell : cells) {
            state.grid[cell.row][cell.col] = 0;
        }

        // nakresli nove pozicie
        for (Vector2D cell : cells) {
            state.grid[cell.row + dy][cell.col + dx] = blockId;
        }

        state.moveCounter ++;
        updateLabel();

        if (isGameWon()) {
            onGameWon();
        }
    }

    private boolean isGameWon() {
        for (int row = 1; row < state.grid.length - 1; row++) {
            for (int col = 1; col < state.grid[0].length - 1; col++) {
                if (state.grid[row][col] > 0) {
                    return false; // este je tam nejaky blok
                }
            }
        }
        return true;
    }

    private void onGameWon() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gratulujem!");
        alert.setHeaderText(null);
        alert.setContentText("Úroveň dokončená! Za 10 sekúnd sa prepneme na ďalšiu úroveň.");
        alert.show(); // show() namiesto showAndWait() - nezablokuje to UI a timer

        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished(e -> {
            alert.close();
            if (state.currentLvl < 6) {
                loadGame(new GameState(0, 0, state.currentLvl + 1));
            }
        });
        delay.play();
    }

    void updateLabel() {
        infoLabel.setText("elapsedTime: " + state.elapsedTime + " number of steps: " + state.moveCounter);
    }

    private void redrawGrid() {
        // prejdi vsetky children gridPane a obnov farby
        for (Node node : gridPane.getChildren()) {
            int r = GridPane.getRowIndex(node);
            int c = GridPane.getColumnIndex(node);
            // nastav farbu podla grid[r][c]
            ColorCell((Pane)node, r, c);
        }
    }

    void ColorCell(Pane cell, int row, int col) {
        int blockID = state.grid[row][col];
        if (blockID == 0) {
            cell.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;");
        } else if (blockID == -1) {
            cell.setStyle("-fx-background-color: black;");
        } else {
            cell.setStyle("-fx-background-color: " + colors[blockID] + ";");
        }
    }
}