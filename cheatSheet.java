package com.particlesdu.pripravanaquadterm;

public class cheatSheet {
    /*
    Animace (Timeline, AnimationTimer)
    Keyboard input (klávesnice)
    Mouse input (myš, klikání)
    Fyzika (pohyb, odrazy, gravitace)
    Geometrie (vzdálenosti, úhly, kolize)
    Časování a state management
    Náhodné hodnoty
    Synchronizace

     */

    import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashSet;
import java.util.Set;

    /**
     * ═══════════════════════════════════════════════════════════════
     * JAVAFX CHEAT-SHEET - VZORY Z TRÉNINKŮ
     * ═══════════════════════════════════════════════════════════════
     *
     * Tento soubor obsahuje všechny opakující se patterny a stavební
     * kameny, které se budete používat v jednotlivých úlohách.
     * Každá metoda je samostatná a má detailní komentáře.
     *
     * Hlavní sekce:
     * 1. ANIMACE A ČASOVÁNÍ
     * 2. KEYBOARD INPUT
     * 3. MOUSE INPUT
     * 4. FYZIKA - POHYB A ODRAZY
     * 5. GEOMETRIE - VZDÁLENOSTI A KOLIZE
     * 6. NÁHODNÉ HODNOTY
     * 7. STATE MANAGEMENT
     * 8. SYNCHRONIZACE (Threading)
     * 9. GRAFIKA A KRESLENÍ
     * ═══════════════════════════════════════════════════════════════
     */

    public class JavaFXCheatSheet {

        // ===== 1. ANIMACE A ČASOVÁNÍ =====

        /**
         * PATTERN: Animace pomocí Timeline (časový interval)
         * Používejte když chcete spustit něco opakovaně v daném intervalu.
         * Vhodné pro: generování nových objektů, kontrolu stavu, aktualizaci logiky
         *
         * Příklady úloh: Generování nového kyvadla každých 10s, Odlétnutí kuličky
         */
        public void animationWithTimeline(Pane root) {
            // Vytvoření Timeline s intervaly
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(100), // Interval 100ms
                            event -> {
                                // KÓD: Co se má dít každých 100ms
                                // Například: přesunout objekt, zkontrolovat stav, vygenerovat nový objekt
                                System.out.println("Tick 100ms");
                            }
                    )
            );

            timeline.setCycleCount(Timeline.INDEFINITE); // Opakuj donekonečna
            timeline.play(); // Spusť animaci

            // STOP: timeline.stop();
        }

        /**
         * PATTERN: Animace pomocí AnimationTimer (smooth frame-by-frame)
         * Používejte pro plynulou animaci - spouští se v refresh rate obrazovky (60fps)
         * Vhodné pro: fyzika, pohyb, detekce kolizí, real-time simulace
         *
         * Příklady úloh: Pohyb opilého námořníka, let míčku, kyvadla, kuličky
         */
        public void animationWithAnimationTimer(Canvas canvas) {
            // Čítač pro počet frámů (debug)
            long[] frameCount = {0};

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    frameCount[0]++;

                    // KÓD: Aktualizace fyziky a stavu
                    GraphicsContext gc = canvas.getGraphicsContext2D();

                    // Vyčistění plátna
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    // Aktualizace pohybu
                    // updatePlayerPosition();
                    // updateBallPhysics();
                    // detectCollisions();

                    // Vykreslení
                    // drawEverything(gc);
                }
            };

            timer.start();
            // STOP: timer.stop();
        }

        /**
         * PATTERN: Měření času pro events (např. "sedí 5 sekund")
         * Uložte čas zahájení a pak porovnávejte s aktuálním časem
         *
         * Příklady úloh: Opilý námořník sedí 5 sekund, měření doby mezi kliknutím
         */
        public boolean hasElapsedTime(long startTimeMillis, long durationMillis) {
            // Aktuální čas
            long currentTime = System.currentTimeMillis();

            // Uplynulo více než požadovaná doba?
            return (currentTime - startTimeMillis) >= durationMillis;
        }

        /**
         * PATTERN: Zvyšování počítadla (počet kroků, bodů, objektů)
         * Jednoduché a účinné
         */
        public int stepCounter = 0;

        public void incrementSteps() {
            stepCounter++;
        }


        // ===== 2. KEYBOARD INPUT =====

        /**
         * PATTERN: Detekce stisku klávesy - setOnKeyPressed
         * Používejte pro okamžitou reakci na klávesu
         * Vhodné pro: ovládání hráče, pohyb figure, akce
         *
         * Příklady úloh: Pong ovládání (a,z,p,l), Kyvadla, 4-hráčové hry
         */
        public void setupKeyboardInput(Scene scene) {
            // Set pro sledování aktuálně stisknutých kláves
            Set<KeyCode> pressedKeys = new HashSet<>();

            scene.setOnKeyPressed(event -> {
                KeyCode code = event.getCode();
                pressedKeys.add(code);

                // Okamžitá reakce na klávesu
                switch (code) {
                    case W:
                    case A:
                        System.out.println("Hráč jde nahoru/vlevo");
                        // movePlayerUp();
                        // movePlayerLeft();
                        break;
                    case S:
                    case D:
                        System.out.println("Hráč jde dolů/vpravo");
                        // movePlayerDown();
                        // movePlayerRight();
                        break;
                    case P:
                        System.out.println("Pauza");
                        // pauseGame();
                        break;
                    case SPACE:
                        System.out.println("Akce");
                        // performAction();
                        break;
                    default:
                        break;
                }

                event.consume();
            });

            scene.setOnKeyReleased(event -> {
                pressedKeys.remove(event.getCode());
                event.consume();
            });

            // V AnimationTimer pak používejte pressedKeys
            // if (pressedKeys.contains(KeyCode.W)) { moveUp(); }
        }

        /**
         * PATTERN: Více kláves najednou (smoothý pohyb)
         * Používejte Set<KeyCode> pressedKeys z setupKeyboardInput
         * Integrujte do AnimationTimer.handle()
         */
        public void handleMultipleKeysPressed(Set<KeyCode> pressedKeys,
                                              double[] playerX, double[] playerY) {
            // Každý frame CheckAte, které klávesy jsou stisknuty
            double speed = 5; // pixely za frame

            if (pressedKeys.contains(KeyCode.W)) {
                playerY[0] -= speed; // Horu
            }
            if (pressedKeys.contains(KeyCode.S)) {
                playerY[0] += speed; // Dolu
            }
            if (pressedKeys.contains(KeyCode.A)) {
                playerX[0] -= speed; // Vlevo
            }
            if (pressedKeys.contains(KeyCode.D)) {
                playerX[0] += speed; // Vpravo
            }
        }


        // ===== 3. MOUSE INPUT =====

        /**
         * PATTERN: Detekce kliknutí myší - setOnMouseClicked
         * Používejte pro jednorázové akce na kliknutí
         * Vhodné pro: zastavení objektu, spuštění, výběr
         *
         * Příklady úloh: Kliknutí na kuličku na kružnici, výběr v Pongu
         */
        public void setupMouseInput(Pane root) {
            root.setOnMouseClicked(event -> {
                double mouseX = event.getX();
                double mouseY = event.getY();

                System.out.println("Kliknutí na: " + mouseX + ", " + mouseY);

                // Zjistěte, jaký objekt byl kliknutý
                // handleClickOnObject(mouseX, mouseY);

                event.consume();
            });
        }

        /**
         * PATTERN: Detekce pohybu myši - setOnMouseMoved
         * Používejte pro sledování pozice myši
         * Vhodné pro: pohyb pálky za myší (Pong variant s myší)
         *
         * Příklady úloh: Pong variant 2 - ovládání myší
         */
        public void setupMouseMovement(Pane root, double[] playerY) {
            root.setOnMouseMoved(event -> {
                double mouseY = event.getY();

                // Pálka seguuje myš (s omezením na hrací plochu)
                playerY[0] = mouseY;

                // Nebo s páskou (smoothing):
                // playerY[0] += (mouseY - playerY[0]) * 0.1;
            });
        }

        /**
         * PATTERN: Detekce kliknutí na konkrétní objekt
         * Kontrolujete, jestli je bod [mouseX, mouseY] uvnitř objektu
         */
        public boolean isClickOnCircle(double mouseX, double mouseY,
                                       double circleX, double circleY, double radius) {
            double distance = Math.sqrt(Math.pow(mouseX - circleX, 2) +
                    Math.pow(mouseY - circleY, 2));
            return distance <= radius;
        }


        // ===== 4. FYZIKA - POHYB A ODRAZY =====

        /**
         * PATTERN: Jednoduchý pohyb v daném směru
         * Směr je úhel v radiánech (0 = vpravo, PI/2 = dolu, PI = vlevo, etc.)
         *
         * Příklady úloh: Opilý námořník, kuličky na kružnici
         */
        public void moveInDirection(double[] position, double angle, double speed) {
            // Rozložení vektoru na komponenty
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;

            position[0] += vx; // x
            position[1] += vy; // y
        }

        /**
         * PATTERN: Náhodný krok (jako opilý námořník)
         * Náhodný směr a náhodná délka
         *
         * Příklady úloh: Opilý námořník - každý krok je náhodný
         */
        public void randomWalk(double[] position, double maxStepLength) {
            // Náhodný směr (0 až 2π radiánů)
            double randomAngle = Math.random() * 2 * Math.PI;

            // Náhodná délka (0 až maxStepLength)
            double randomLength = Math.random() * maxStepLength;

            // Pohyb
            moveInDirection(position, randomAngle, randomLength);
        }

        /**
         * PATTERN: Odrazy (reflexe) - ideální odraz (bez ztráty energie)
         * Používejte když odraz bez fyziky
         */
        public double[] reflectVector(double vx, double vy, double normalX, double normalY) {
            // Normála musí být normalizovaná!
            // Vzorec: v' = v - 2(v·n)n

            double dotProduct = vx * normalX + vy * normalY;
            double reflectedVx = vx - 2 * dotProduct * normalX;
            double reflectedVy = vy - 2 * dotProduct * normalY;

            return new double[]{reflectedVx, reflectedVy};
        }

        /**
         * PATTERN: Odrazy s ztrátou energie (realisticko - gumová míčka)
         * Energetický koeficient: 0 = bez odrazu, 1 = ideální odraz
         *
         * Příklady úloh: Odskakující míčka
         */
        public double[] reflectWithEnergyLoss(double vx, double vy,
                                              double normalX, double normalY,
                                              double energyCoefficient) {
            double[] reflected = reflectVector(vx, vy, normalX, normalY);

            // Zmenšení magnitude vektoru
            reflected[0] *= energyCoefficient;
            reflected[1] *= energyCoefficient;

            return reflected;
        }

        /**
         * PATTERN: Gravitace (svislý pohyb)
         * Aplikujte při každé aktualizaci fyziky
         *
         * Příklady úloh: Odskakující míčka, virus simulace
         */
        public void applyGravity(double[] velocity, double gravity) {
            velocity[1] += gravity; // gravity je obvykle kladné (dolů)
        }

        /**
         * PATTERN: Odpor vzduchu (trenie, zpomalení)
         * Aplikujte při každé aktualizaci fyziky
         *
         * Příklady úloh: Kyvadla zpomalují, míčka zpomalují
         */
        public void applyDrag(double[] velocity, double dragCoefficient) {
            // dragCoefficient je mezi 0.9 až 0.99
            // Čím blíž 1, tím méně se zpomaluje
            velocity[0] *= dragCoefficient;
            velocity[1] *= dragCoefficient;
        }

        /**
         * PATTERN: Fyzika kyvadla
         * úhel = aktuální úhel v radiánech
         * angularVelocity = úhlová rychlost
         * length = délka lanka
         * gravity = 9.81 nebo nějaká konstanta
         *
         * Příklady úloh: Kyvadla
         */
        public void updatePendulum(double[] angle, double[] angularVelocity,
                                   double length, double gravity, double damping) {
            // Aproximace: a = -(g/L) * sin(angle)
            double angularAcceleration = -(gravity / length) * Math.sin(angle[0]);

            // Aktualizace úhlové rychlosti
            angularVelocity[0] += angularAcceleration;

            // Aplikace odporu (disipace energie)
            angularVelocity[0] *= damping; // damping ~ 0.99

            // Aktualizace úhlu
            angle[0] += angularVelocity[0];
        }

        /**
         * PATTERN: Pohyb na kružnici (kuličky na kruhové dráze)
         * Máte úhel a poloměr, chcete xy souřadnice
         *
         * Příklady úloh: Kuličky na kružnici
         */
        public void updatePositionOnCircle(double[] position,
                                           double[] angle, double[] angularVelocity,
                                           double radius, double centerX, double centerY) {
            // Aktualizace úhlu
            angle[0] += angularVelocity[0];

            // Konverze úhel -> xy
            position[0] = centerX + radius * Math.cos(angle[0]);
            position[1] = centerY + radius * Math.sin(angle[0]);
        }


        // ===== 5. GEOMETRIE - VZDÁLENOSTI A KOLIZE =====

        /**
         * PATTERN: Vzdálenost mezi dvěma body
         * Základní geometrie - používejte všude kde potřebujete zjistit
         * jestli se objekty "setkaly" nebo "srazily"
         *
         * Příklady úloh: Opilý námořník padá do moře (vzdálenost od středu),
         *               Dva námořníci se setkávají, Kuličky se srazí
         */
        public double distance(double x1, double y1, double x2, double y2) {
            return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }

        /**
         * PATTERN: Detekce kolize - kružnice vs. kružnice
         * Dvě kuličky se srazí, když je vzdálenost mezi nimi <= r1 + r2
         *
         * Příklady úloh: Virus simulace, kuličky na kružnici, Pong
         */
        public boolean isColliding(double x1, double y1, double r1,
                                   double x2, double y2, double r2) {
            double dist = distance(x1, y1, x2, y2);
            return dist <= (r1 + r2);
        }

        /**
         * PATTERN: Detekce kolize - bod vs. kružnice (zda-li je bod uvnitř)
         * Bod je uvnitř když je vzdálenost <= poloměr
         *
         * Příklady úloh: Kliknutí na kuličku
         */
        public boolean isPointInCircle(double pointX, double pointY,
                                       double circleX, double circleY, double radius) {
            return distance(pointX, pointY, circleX, circleY) <= radius;
        }

        /**
         * PATTERN: Detekce kolize - bod vs. obdélník
         * Bod je uvnitř když pointX je mezi x1 a x2 a pointY mezi y1 a y2
         *
         * Příklady úloh: Detekce, kdy míček narazí do pálky
         */
        public boolean isPointInRectangle(double pointX, double pointY,
                                          double rectX, double rectY,
                                          double rectWidth, double rectHeight) {
            return pointX >= rectX && pointX <= rectX + rectWidth &&
                    pointY >= rectY && pointY <= rectY + rectHeight;
        }

        /**
         * PATTERN: Detekce kolize - kružnice vs. obdélník (aproximace)
         * Jednoduché řešení: střed kuličky vs. obdélník + kontrola vzdálenosti
         */
        public boolean isCircleCollidingWithRectangle(double circleX, double circleY, double radius,
                                                      double rectX, double rectY,
                                                      double rectWidth, double rectHeight) {
            // Nejbližší bod na obdélníku k středu kuličky
            double closestX = Math.max(rectX, Math.min(circleX, rectX + rectWidth));
            double closestY = Math.max(rectY, Math.min(circleY, rectY + rectHeight));

            // Vzdálenost mezi středem kuličky a nejbližším bodem
            double distX = circleX - closestX;
            double distY = circleY - closestY;

            return (distX * distX + distY * distY) <= (radius * radius);
        }

        /**
         * PATTERN: Detekce hranice - jsou mimo plochu?
         * Vhodné pro: opilý námořník padá do moře, míček mimo obrazovku
         */
        public boolean isOutOfBounds(double x, double y, double canvasWidth, double canvasHeight) {
            return x < 0 || x > canvasWidth || y < 0 || y > canvasHeight;
        }

        /**
         * PATTERN: Detekce hranice - kruh (bod je mimo kruh)
         * Vhodné pro: opilý námořník padá do moře (kruhové mólo)
         *
         * Příklady úloh: Opilý námořník
         */
        public boolean isOutsideCircle(double x, double y, double centerX, double centerY,
                                       double radius) {
            return distance(x, y, centerX, centerY) > radius;
        }

        /**
         * PATTERN: Úhel mezi dvěma body (atan2)
         * Vhodné pro: směr z bodu A k bodu B
         */
        public double angleToPoint(double fromX, double fromY, double toX, double toY) {
            return Math.atan2(toY - fromY, toX - fromX);
        }

        /**
         * PATTERN: Normalizace vektoru (jednotkový vektor)
         * Vhodné pro: směr bez magnitude
         */
        public double[] normalizeVector(double vx, double vy) {
            double magnitude = Math.sqrt(vx * vx + vy * vy);

            if (magnitude == 0) {
                return new double[]{0, 0};
            }

            return new double[]{vx / magnitude, vy / magnitude};
        }


        // ===== 6. NÁHODNÉ HODNOTY =====

        /**
         * PATTERN: Náhodné číslo v rozsahu [min, max]
         * Používejte všude kde potřebujete náhodu
         */
        public double randomInRange(double min, double max) {
            return min + Math.random() * (max - min);
        }

        /**
         * PATTERN: Náhodné číslo v rozsahu [min, max] - celočíselné
         */
        public int randomIntInRange(int min, int max) {
            return min + (int)(Math.random() * (max - min + 1));
        }

        /**
         * PATTERN: Náhodná barva (RGB)
         */
        public Color randomColor() {
            return Color.color(Math.random(), Math.random(), Math.random());
        }

        /**
         * PATTERN: Výběr náhodné barvy ze seznamu
         */
        public Color getRandomColorFromList(Color[] colors) {
            return colors[randomIntInRange(0, colors.length - 1)];
        }

        /**
         * PATTERN: Náhodný úhel (0 až 2π)
         */
        public double randomAngle() {
            return Math.random() * 2 * Math.PI;
        }

        /**
         * PATTERN: Náhodný jednotkový vektor (směr)
         */
        public double[] randomDirection() {
            double angle = randomAngle();
            return new double[]{Math.cos(angle), Math.sin(angle)};
        }

        /**
         * PATTERN: Náhodný vektor s danou magnitutou
         */
        public double[] randomVectorWithMagnitude(double magnitude) {
            double[] direction = randomDirection();
            return new double[]{direction[0] * magnitude, direction[1] * magnitude};
        }


        // ===== 7. STATE MANAGEMENT =====

        /**
         * PATTERN: Objekty se stavy (Enum)
         * Vhodné pro: opilý námořník (pohyb vs. sezení), virus (zdravý/nemocný/vyléčený)
         *
         * Příklady úloh: Opilý námořník, virus simulace
         */
        public enum State {
            MOVING, SITTING, FALLING, DEAD
        }

        /**
         * PATTERN: Třída pro objekt se stavem (příklad)
         * Každý objekt si hlídá svůj stav a čas
         */
        public static class SailorObject {
            public double x, y;
            public State state;
            public long sitStartTime;

            public SailorObject(double x, double y) {
                this.x = x;
                this.y = y;
                this.state = State.MOVING;
                this.sitStartTime = 0;
            }

            public void sitDown() {
                this.state = State.SITTING;
                this.sitStartTime = System.currentTimeMillis();
            }

            public boolean hasSatEnough(long durationMs) {
                if (state != State.SITTING) return false;
                return System.currentTimeMillis() - sitStartTime >= durationMs;
            }
        }

        /**
         * PATTERN: Globální stav hry
         * Vhodné pro: Pong (score, stav hry), virus (počet nemocných)
         */
        public static class GameState {
            public int player1Score = 0;
            public int player2Score = 0;
            public boolean isPaused = false;
            public int totalBalls = 0;

            public void incrementScore(int playerNumber) {
                if (playerNumber == 1) player1Score++;
                else player2Score++;
            }

            public void togglePause() {
                isPaused = !isPaused;
            }
        }


        // ===== 8. SYNCHRONIZACE (Threading) =====

        /**
         * PATTERN: Synchronized přístup k sdílenému zdroji
         * Vhodné pro: více vláken které mění stejný seznam/pole
         *
         * Příklady úloh: Dva námořníci (dva threads), virus simulace, kuličky
         */
        public class SharedDataContainer {
            private java.util.List<SailorObject> sailors = new java.util.ArrayList<>();

            // Synchronized metoda - jen jedno vlákno v jednom čase!
            public synchronized void addSailor(SailorObject sailor) {
                sailors.add(sailor);
            }

            public synchronized void removeSailor(int index) {
                sailors.remove(index);
            }

            public synchronized int getSailorCount() {
                return sailors.size();
            }

            // Bezpečné iterování
            public synchronized void updateAllSailors() {
                for (SailorObject sailor : sailors) {
                    // Aktualizuj námořníka
                }
            }

            // Kopie seznamu pro iterování bez zámku
            public synchronized java.util.List<SailorObject> getSailorsCopy() {
                return new java.util.ArrayList<>(sailors);
            }
        }


        // ===== 9. GRAFIKA A KRESLENÍ =====

        /**
         * PATTERN: Vykreslení kruhu (kuličky)
         */
        public void drawCircle(GraphicsContext gc, double x, double y,
                               double radius, Color color) {
            gc.setFill(color);
            gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        }

        /**
         * PATTERN: Vykreslení obdélníku (pálka, stěna)
         */
        public void drawRectangle(GraphicsContext gc, double x, double y,
                                  double width, double height, Color color) {
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        }

        /**
         * PATTERN: Vykreslení linie (trať kyvadla, míče)
         */
        public void drawLine(GraphicsContext gc, double x1, double y1,
                             double x2, double y2, Color color, double width) {
            gc.setStroke(color);
            gc.setLineWidth(width);
            gc.strokeLine(x1, y1, x2, y2);
        }

        /**
         * PATTERN: Vykreslení textu (skóre, info)
         */
        public void drawText(GraphicsContext gc, String text, double x, double y,
                             Color color, int fontSize) {
            gc.setFill(color);
            gc.setFont(new javafx.scene.text.Font(fontSize));
            gc.fillText(text, x, y);
        }

        /**
         * PATTERN: Vykreslení bodu (střed kružnice, středová značka)
         */
        public void drawPoint(GraphicsContext gc, double x, double y,
                              double size, Color color) {
            gc.setFill(color);
            gc.fillOval(x - size/2, y - size/2, size, size);
        }

        /**
         * PATTERN: Vykreslení kyvadla (linka + kulička)
         * x, y = bod zavěšení
         * angle = úhel v radiánech
         * length = délka lanka
         * ballRadius = poloměr kuličky
         */
        public void drawPendulum(GraphicsContext gc, double x, double y,
                                 double angle, double length, double ballRadius,
                                 Color lineColor, Color ballColor) {
            // Konec lanka (pozice kuličky)
            double ballX = x + length * Math.sin(angle);
            double ballY = y + length * Math.cos(angle);

            // Nakreslení lanka
            drawLine(gc, x, y, ballX, ballY, lineColor, 2);

            // Nakreslení kuličky
            drawCircle(gc, ballX, ballY, ballRadius, ballColor);
        }

        /**
         * PATTERN: Vykreslení kruhu (hrací plocha - mólo)
         */
        public void drawCircleOutline(GraphicsContext gc, double centerX, double centerY,
                                      double radius, Color color, double lineWidth) {
            gc.setStroke(color);
            gc.setLineWidth(lineWidth);
            gc.strokeOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
        }


        // ===== KOMBINOVANÉ PŘÍKLADY =====

        /**
         * PŘÍKLAD 1: Jednoduchý opilý námořník (loop)
         * Ukazuje kombinaci: random walk, detekce hranice, počítání, vykreslení
         */
        public void exampleDrunkSailor() {
            double[] position = {0, 0}; // střed móla
            double centerX = 100, centerY = 100;
            double moleRadius = 10 * 50; // 10 metrů * 50px
            int steps = 0;

            // V AnimationTimer:
            // while (steps < maxSteps) {
            //     randomWalk(position, 1);
            //     if (isOutsideCircle(position[0] + centerX, position[1] + centerY,
            //                        centerX, centerY, moleRadius)) {
            //         // Padl do moře!
            //         break;
            //     }
            //     steps++;
            // }
        }

        /**
         * PŘÍKLAD 2: Detekce setkání dvou námořníků
         * Ukazuje kombinaci: vzdálenost, stav, časování
         */
        public void exampleTwoSailorsCollision(SailorObject sailor1, SailorObject sailor2) {
            double collisionDistance = 2 * 50; // 2 metry * 50px

            if (distance(sailor1.x, sailor1.y, sailor2.x, sailor2.y) <= collisionDistance) {
                // Setkali se!
                sailor1.sitDown();
                sailor2.sitDown();
            }
        }

        /**
         * PŘÍKLAD 3: Detekce kliknutí na kuličku na kružnici
         * Ukazuje kombinaci: mouse input, kolize bod-kruh, state
         */
        public void exampleClickOnBall(MouseEvent event, java.util.List<Circle> balls) {
            double mouseX = event.getX();
            double mouseY = event.getY();

            for (Circle ball : balls) {
                if (isPointInCircle(mouseX, mouseY, ball.getCenterX(), ball.getCenterY(),
                        ball.getRadius())) {
                    // Klikl na tuto kuličku!
                    // Změň stav (zastavení/spuštění)
                    break;
                }
            }
        }

        /**
         * PŘÍKLAD 4: Odrazy míčku od pálky v Pongu
         * Ukazuje kombinaci: kolize kružnice-obdélník, odrazy, fyzika
         */
        public void exampleBallPaddleCollision(double ballX, double ballY, double ballRadius,
                                               double[] ballVelocity,
                                               double paddleX, double paddleY,
                                               double paddleWidth, double paddleHeight) {
            if (isCircleCollidingWithRectangle(ballX, ballY, ballRadius,
                    paddleX, paddleY, paddleWidth, paddleHeight)) {
                // Normála pálky je horizontální
                double[] normal = {-1, 0}; // nebo {1, 0} v závislosti na straně

                double[] reflected = reflectVector(ballVelocity[0], ballVelocity[1],
                        normal[0], normal[1]);
                ballVelocity[0] = reflected[0];
                ballVelocity[1] = reflected[1];
            }
        }
    }

/**
 * ═══════════════════════════════════════════════════════════════
 * SHRNUTÍ NEJDŮLEŽITĚJŠÍCH PATTERNŮ PRO VAŠE ÚLOHY
 * ═══════════════════════════════════════════════════════════════
 *
 * ✓ ANIMACE:
 *   - AnimationTimer.handle() pro fyziku a pohyb (60fps)
 *   - Timeline pro časované eventy (generování objektů, atd.)
 *   - System.currentTimeMillis() pro kontrolu uplynulého času
 *
 * ✓ INPUT:
 *   - setOnKeyPressed + Set<KeyCode> pressedKeys pro plynulý pohyb
 *   - setOnMouseClicked pro jednorázové akce
 *   - setOnMouseMoved pro pohyb myší
 *
 * ✓ FYZIKA:
 *   - moveInDirection() pro pohyb
 *   - applyGravity(), applyDrag() pro přírodu
 *   - reflectVector() pro odrazy
 *   - updatePendulum() pro kyvadla
 *
 * ✓ GEOMETRIE:
 *   - distance() pro detekci setkání
 *   - isColliding(), isPointInCircle(), isCircleCollidingWithRectangle()
 *   - isOutOfBounds(), isOutsideCircle()
 *   - angleToPoint() pro směry
 *
 * ✓ RANDOM:
 *   - randomInRange(), randomIntInRange()
 *   - randomAngle(), randomDirection()
 *
 * ✓ STATE:
 *   - Enum State pro objekty
 *   - Timestamp (System.currentTimeMillis()) pro časované stavy
 *   - Synchronized metody pro vlákna
 *
 * ✓ GRAFIKA:
 *   - drawCircle(), drawRectangle(), drawLine(), drawText()
 *   - drawPendulum() pro kyvadla
 *
 * Kdy co použít:
 * Pohyb, odrazy, fyzika              -> AnimationTimer, moveInDirection, reflect
 * Klávesnice                         -> setOnKeyPressed + Set pressedKeys
 * Myš                                -> setOnMouseClicked, isPointInCircle
 * Detekce kolizí                     -> distance, isColliding
 * Časované eventy (nové objekty)    -> Timeline
 * Měření doby (sedí 5 sekund)       -> System.currentTimeMillis(), hasElapsedTime
 * Více objektů s stavy               -> List<Object> + synchronized
 * Kreslení                           -> Canvas + drawXxx metody
 *
 * ═══════════════════════════════════════════════════════════════
 */

}
