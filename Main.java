package com.particlesdu.pripravanaquadterm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
*/

public class Main extends Application {

    public static final int windowSize = 800;
    Pane root = new Pane();
    private ArrayList<Turtle> turtles = new ArrayList<>();


    @Override
    public void start(Stage stage) throws Exception {
        root.setPrefSize(windowSize, windowSize);
        readFile("turtles.txt");
        // Keď klikneme do plochy, nájdeme najbližšiu korytnačku
        // a pozastavíme alebo spustíme ju
        root.setOnMouseClicked(event -> {
            toggleNearestTurtle(event.getX(), event.getY());
        });

        //vytvorime scenu a nastavime jej root node
        Scene scene = new Scene(root, windowSize, windowSize);
        //nazov okna
        stage.setTitle("korytnacky");
        //priradie scenu do okna
        stage.setScene(scene);
        //zapneme to
        stage.show();

        // Spustíme všetky korytnačky
        for (Turtle turtle : turtles) {
            turtle.start();
        }
    }

    private void readFile(String filename) throws Exception {

        // Súbor načítavame z priečinka resources.
        // Lomka "/" znamená, že hľadáme priamo v src/main/resources.
        InputStream input = getClass().getResourceAsStream("/" + filename);

        // Ak sa súbor nenašiel, input bude null.
        // Vtedy radšej vyhodíme vlastnú chybu s jasnou hláškou.
        if (input == null) {
            throw new RuntimeException("Súbor " + filename + " sa nenašiel v resources.");
        }

        // InputStream prevedieme na textový reader
        // a načítame všetky riadky zo súboru do zoznamu.
        List<String> lines = new BufferedReader(
                new InputStreamReader(input)
        ).lines().toList();

        // id je poradové číslo korytnačky.
        // Prvý riadok súboru = korytnačka číslo 1.
        int id = 1;

        // Každý riadok súboru predstavuje program jednej korytnačky.
        for (String line : lines) {

            // Ak je riadok prázdny, preskočíme ho.
            if (line.isBlank()) {
                continue;
            }

            // Inštrukcie sú oddelené bodkočiarkou.
            // Napríklad:
            // STEP 5;TURN +90;FORWARD 10
            String[] parts = line.split(";");

            // Sem uložíme všetky inštrukcie pre jednu korytnačku.
            ArrayList<Instruction> instructions = new ArrayList<>();

            // Každú textovú inštrukciu prevedieme na objekt Instruction.
            for (String part : parts) {
                instructions.add(new Instruction(part));
            }

            // Vytvoríme novú korytnačku.
            // Dostane svoje id, kresliacu plochu root a svoj program.
            Turtle turtle = new Turtle(id, root, instructions);

            // Pridáme korytnačku do zoznamu všetkých korytnačiek.
            turtles.add(turtle);

            // Ďalší neprázdny riadok bude ďalšia korytnačka.
            id++;
        }
    }

    private void toggleNearestTurtle(double mouseX, double mouseY) {
        // Najbližšia korytnačka zatiaľ neexistuje
        Turtle nearest = null;

        // Najlepšia nájdená vzdialenosť
        double bestDistance = Double.MAX_VALUE;

        // Prejdeme všetky korytnačky
        for (Turtle turtle : turtles) {

            // Ak už korytnačka skončila, ignorujeme ju
            if (!turtle.isAliveTurtle()) {
                continue;
            }

            // Vypočítame vzdialenosť korytnačky od kliknutia
            double distance = turtle.distanceTo(mouseX, mouseY);

            // Ak je táto korytnačka bližšie než doterajšia,
            // zapamätáme si ju
            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = turtle;
            }
        }

        // Ak sme nejakú živú korytnačku našli,
        // zmeníme jej stav: beží <-> stojí
        if (nearest != null) {
            nearest.togglePaused();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}