package org.cartetresor.models;

import java.util.Objects;

public class LineData {
    private int x;
    private int y;
    private int nbOfTreasures;

    public LineData(int x, int y, int nbOfTreasures) {
        this.x = x;
        this.y = y;
        this.nbOfTreasures = nbOfTreasures;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNbOfTreasures() {
        return nbOfTreasures;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LineData lineData = (LineData) o;
        return x == lineData.x && y == lineData.y && nbOfTreasures == lineData.nbOfTreasures;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, nbOfTreasures);
    }
}
