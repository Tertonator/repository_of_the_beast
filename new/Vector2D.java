package com.particlesdu.demo2;

import java.util.Objects;

public class Vector2D {
    public int col;
    public int row;

    public Vector2D(int col,int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass()) //getclass vrati skutocny typ triedy cize osetrujeme ci aj obj je typu Vector2D (porovnavame ci su to vobec rovnake objekty
            return false;

        Vector2D other = (Vector2D) obj;
        return col == other.col && row == other.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    public Vector2D oneLeft() {
        return new Vector2D(col -1, row);
    }
    public Vector2D oneRight() {
        return new Vector2D(col +1, row);
    }
    public Vector2D oneUp() {
        return new Vector2D(col, row -1);
    }
    public Vector2D oneDown() {
        return new Vector2D(col, row +1);
    }
}
