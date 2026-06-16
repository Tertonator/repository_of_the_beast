package com.example.demo;

import java.util.*;

public class GridAnalyzer {
    ColorGrid grid;

    public GridAnalyzer(ColorGrid grid_in) {
        grid = grid_in;
    }

    Set<Vector2D> visited = new HashSet<>();
    Set<Integer> seenColors = new HashSet<>();
    ArrayList<Integer> blobSizes = new ArrayList<>();

    public void analyzeGrid() {
        //reset values:
        visited = new HashSet<>();
        seenColors = new HashSet<>();
        blobSizes = new ArrayList<>();

        for(int y = 0; y < grid.height(); y++) {
            for(int x = 0; x < grid.width(); x++) {
                if(!visited.contains(new Vector2D(x, y))) {
                    blobSizes.add(recursiveMeasureBlob(new Vector2D(x,y)));
                }
            }
        }

        Collections.sort(blobSizes);

        System.out.println("velkost najvacsej suvislej: " + blobSizes.getLast()
                         + ", pocet roznych farieb: " + seenColors.size());
    }

    int recursiveMeasureBlob(Vector2D pos) {
        if(visited.contains(pos)) return 0;

        visited.add(pos);

        if(!seenColors.contains(grid.getValue(pos))) {
            seenColors.add(grid.getValue(pos));
        };

        int currentValue = grid.getValue(pos);
        int output = 1;

        //left
        if (pos.x > 0) {
            if(grid.getValue(pos.oneLeft()) == currentValue)
            {
                output += recursiveMeasureBlob(pos.oneLeft());
            }
        }
        //right
        if (pos.x < grid.width()-1) {
            if(grid.getValue(pos.oneRight()) == currentValue)
            {
                output += recursiveMeasureBlob(pos.oneRight());
            }
        }
        //top
        if (pos.y > 0) {
            if(grid.getValue(pos.oneUp()) == currentValue)
            {
                output += recursiveMeasureBlob(pos.oneUp());
            }
        }
        //bellow
        if (pos.y < grid.height()-1) {
            if(grid.getValue(pos.oneDown()) == currentValue)
            {
                output += recursiveMeasureBlob(pos.oneDown());
            }
        }
        return output;
    }
}
