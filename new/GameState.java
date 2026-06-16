package com.particlesdu.demo2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {
    public int[][] grid ;
    public int elapsedTime;
    public int moveCounter;
    public int currentLvl;

    public GameState(int elapsedTime, int moveCounter, int currentLvl) {
        this.elapsedTime = elapsedTime;
        this.moveCounter = moveCounter;
        this.currentLvl = currentLvl;

        try {
            grid = readFile("blocks" + currentLvl + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GameState() {
        // prazdny konstruktor pre load()
    }

    public void save(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(elapsedTime);
            writer.println(moveCounter);
            writer.println(currentLvl);

            for (int[] row : grid) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) sb.append(" ");
                    sb.append(row[i]);
                }
                writer.println(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Chyba pri ukladani: " + e.getMessage());
        }
    }

    public static GameState load(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            GameState state = new GameState();
            state.elapsedTime = Integer.parseInt(reader.readLine().trim());
            state.moveCounter = Integer.parseInt(reader.readLine().trim());
            state.currentLvl = Integer.parseInt(reader.readLine().trim());

            List<int[]> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.trim().split("\\s+"); //totalne zbavenie sa medzier
                int[] row = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    row[i] = Integer.parseInt(parts[i]);
                }
                rows.add(row);
            }

            state.grid = rows.toArray(new int[0][]);
            return state;

        } catch (IOException e) {
            System.err.println("Chyba pri nacitani: " + e.getMessage());
            return null;
        }
    }

    public int[][] readFile(String filename) throws IOException {

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

        int[][] grid = new int[lines.size()][];

        // Každý riadok súboru predstavuje program jednej korytnačky.
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);


            // Inštrukcie sú oddelené medzerou.
            //String[] parts = line.split(" "); toto nefunguje dobre pre viacere medzery vedla seba
            String[] parts = line.trim().split("\\s+");
            grid[i] = new int[parts.length];

            // Každú textovú inštrukciu prevedieme na objekt Instruction.
            for (int i2 = 0; i2 < parts.length; i2++) {
                grid[i][i2] = Integer.parseInt(parts[i2]);
            }
        }

        AnalyseGrid(grid);
        return grid;
    }

    public void AnalyseGrid(int[][] grid) {
        Map<Integer, ArrayList<Vector2D>> blocks = new HashMap<>();

        // Namapovanie blokov
        for (int row = 1; row < grid.length-1; row++) { //zaiame na 1 a koncime na -1 lebo nechceme analzyovat okraje
            for (int col = 1; col < grid[row].length-1; col++) {
                int blockId = grid[row][col];

                if (blockId > 0) {
                    ArrayList<Vector2D> block = blocks.get(blockId);

                    if (block == null) {
                        block = new ArrayList<>();
                        blocks.put(blockId, block);
                    }

                    block.add(new Vector2D(col, row));
                }
            }
        }

        int amountOfRectangularBlocks = 0;

        // Spracovanie jednotlivých blokov
        for (Map.Entry<Integer, ArrayList<Vector2D>> entry : blocks.entrySet()) {

            int blockId = entry.getKey();
            ArrayList<Vector2D> block = entry.getValue();

            int leftMost = Integer.MAX_VALUE;
            int rightMost = Integer.MIN_VALUE;
            int topMost = Integer.MAX_VALUE;
            int bottomMost = Integer.MIN_VALUE;

            for (Vector2D v : block) {

                if (v.col < leftMost) {
                    leftMost = (int) v.col;
                }

                if (v.col > rightMost) {
                    rightMost = (int) v.col;
                }

                if (v.row < topMost) {
                    topMost = (int) v.row;
                }

                if (v.row > bottomMost) {
                    bottomMost = (int) v.row;
                }
            }

            boolean isRectangle = true;

            for (int row = topMost; row <= bottomMost && isRectangle; row++) {
                for (int col = leftMost; col <= rightMost; col++) {

                    if (grid[row][col] != blockId) {
                        isRectangle = false;
                        break;
                    }
                }
            }

            if (isRectangle) {
                amountOfRectangularBlocks++;
            }
        }

        System.out.println("Rectangular blocks: " + amountOfRectangularBlocks);
    }
}
