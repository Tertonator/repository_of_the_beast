package com.example.demo;

import java.util.Objects;

public class Vector2D {
    public int x;
    public int y;

    public Vector2D(int posX,int posY) {
        x = posX;
        y = posY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass()) //getclass vrati skutocny typ triedy cize osetrujeme ci aj obj je typu Vector2D (porovnavame ci su to vobec rovnake objekty
            return false;

        Vector2D other = (Vector2D) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    public Vector2D oneLeft() {
        return new Vector2D(x-1,y);
    }
    public Vector2D oneRight() {
        return new Vector2D(x+1,y);
    }
    public Vector2D oneUp() {
        return new Vector2D(x,y-1);
    }
    public Vector2D oneDown() {
        return new Vector2D(x,y+1);
    }
}
