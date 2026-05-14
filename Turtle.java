package com.particlesdu.pripravanaquadterm;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.ArrayList;

import static com.particlesdu.pripravanaquadterm.InstructionType.*;

public class Turtle extends Thread {
    // Poradové číslo korytnačky
    private int id;

    // Plocha, do ktorej kreslíme
    private Pane root;

    // Program korytnačky = zoznam inštrukcií
    private ArrayList<Instruction> instructions;

    // Logická pozícia korytnačky.
    // [0, 0] znamená stred okna.
    private double x = 0;
    private double y = 0;

    // Uhol natočenia v stupňoch
    private int angle = 0;

    // Veľkosť jedného kroku
    private int stepSize = 0;

    // Čas čakania medzi inštrukciami v milisekundách
    private int sleepTime = 0;

    // Farba čiary
    private Color color = Color.BLACK;

    // Či korytnačka ešte žije
    private boolean alive = true;

    // Či je pozastavená kliknutím
    private boolean paused = false;

    // Celková prejdená dráha v pixeloch
    private double pathLength = 0;

    // Obrázok korytnačky na scéne
    private ImageView icon;

    public Turtle(int id, Pane root, ArrayList<Instruction> instructions) {
        this.id = id;
        this.root = root;
        this.instructions = instructions;

        // Načítanie obrázka korytnačky.
        // Súbor turtle.png musí byť v resources alebo v správnom priečinku projektu.
        Image image = new Image("turtle.png");

        icon = new ImageView(image);
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        // GUI môžeme meniť iba cez Platform.runLater,
        // lebo táto trieda bude bežať ako vlastné vlákno.
        Platform.runLater(() -> {
            icon.setLayoutX(Main.windowSize / 2.0 - 15);
            icon.setLayoutY(Main.windowSize / 2.0 - 15);
            root.getChildren().add(icon);
        });
    }

    @Override
    public void run() {
        int index = 0;

        // Program korytnačky sa vykonáva stále dokola,
        // kým korytnačka nevybehne mimo okna.
        while (alive) {

            // Ak je pozastavená, nič nevykonáva,
            // iba krátko čaká, aby zbytočne nezaťažovala procesor.
            if (paused) {
                sleep(50);
                continue;
            }

            // Vyberieme aktuálnu inštrukciu
            Instruction instruction = instructions.get(index);

            // Vykonáme ju
            execute(instruction);

            // Posunieme sa na ďalšiu inštrukciu
            index++;

            // Ak sme na konci programu, začneme od začiatku
            if (index >= instructions.size()) {
                index = 0;
            }

            // Po každej inštrukcii počkáme podľa sleepTime
            sleep(sleepTime);
        }
        // Keď korytnačka skončí, vypíše poradové číslo a prejdenú trasu
        System.out.println("Turtle " + id + ": " + pathLength);
    }

    private void execute(Instruction instruction) {
        switch (instruction.type) {

            case SLEEP:
                // Nastaví alebo upraví sleepTime
                sleepTime = instruction.applyTo(sleepTime);

                // Sleep nemôže byť záporný
                if (sleepTime < 0) {
                    sleepTime = 0;
                }
                break;

            case STEP:
                // Nastaví alebo upraví veľkosť kroku
                stepSize = instruction.applyTo(stepSize);
                break;

            case TURN:
                // Nastaví alebo upraví uhol
                angle = instruction.applyTo(angle);
                break;

            case COLOR:
                // Farbu si prevedieme na int,
                // upravíme podľa argumentu a potom naspäť na Color.
                int colorInt = colorToInt(color);
                colorInt = instruction.applyTo(colorInt);

                color = Color.rgb(
                        colorInt >> 16 & 0xff,
                        colorInt >> 8 & 0xff,
                        colorInt & 0xff
                );
                break;

            case FORWARD:
                // FORWARD používa argument ako násobok stepSize
                moveForward(instruction.signedValue());
                break;
        }
    }

    private void moveForward(int arg) {
        // Zapamätáme si starú pozíciu
        double oldX = x;
        double oldY = y;

        // Skutočná dĺžka pohybu v pixeloch
        double distance = arg * stepSize;

        // Výpočet novej logickej pozície podľa uhla
        double newX = x + distance * Math.cos(Math.toRadians(angle));
        double newY = y + distance * Math.sin(Math.toRadians(angle));

        // Do prejdenej dráhy rátame absolútnu dĺžku úsečky
        pathLength += Math.abs(distance);

        // Prevod logických súradníc na obrazovkové.
        // Logické [0,0] je stred okna.
        // Obrazovkové [0,0] je ľavý horný roh.
        double screenOldX = Main.windowSize / 2.0 + oldX;
        double screenOldY = Main.windowSize / 2.0 + oldY;
        double screenNewX = Main.windowSize / 2.0 + newX;
        double screenNewY = Main.windowSize / 2.0 + newY;

        // Kreslenie čiary a posun obrázka musí ísť cez JavaFX vlákno
        Platform.runLater(() -> {
            // Čiara od starej pozície po novú
            Line line = new Line(screenOldX, screenOldY, screenNewX, screenNewY);
            line.setStroke(color);
            line.setStrokeWidth(2);

            root.getChildren().add(line);

            // Posunieme obrázok korytnačky na novú pozíciu
            icon.setLayoutX(screenNewX - icon.getFitWidth() / 2);
            icon.setLayoutY(screenNewY - icon.getFitHeight() / 2);

            // Natočíme obrázok v smere pohybu.
            // +90 závisí od toho, ako je obrázok natočený v súbore turtle.png.
            icon.setRotate(angle + 90);

            // Obrázok dáme dopredu, aby nebol pod čiarami
            icon.toFront();
        });

        // Aktualizujeme logickú pozíciu
        x = newX;
        y = newY;

        // Ak vybehla mimo okna, simulácia tejto korytnačky končí
        if (screenNewX < 0 || screenNewX > Main.windowSize ||
                screenNewY < 0 || screenNewY > Main.windowSize) {
            alive = false;
        }
    }

    public double distanceTo(double mouseX, double mouseY) {
        // Aktuálna pozícia korytnačky v obrazovkových súradniciach
        double screenX = Main.windowSize / 2.0 + x;
        double screenY = Main.windowSize / 2.0 + y;

        double dx = screenX - mouseX;
        double dy = screenY - mouseY;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public void togglePaused() {
        paused = !paused;
    }

    public boolean isAliveTurtle() {
        return alive;
    }

    private int colorToInt(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        return (r << 16) | (g << 8) | b;
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
