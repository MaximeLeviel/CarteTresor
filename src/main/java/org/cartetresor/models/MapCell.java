package org.cartetresor.models;

public class MapCell {
    private boolean mountain;
    private int treasuresCount = 0;

    public boolean isMountain() {
        return mountain;
    }

    public void setMountain(boolean mountain) {
        this.mountain = mountain;
    }

    public int getTreasuresCount() {
        return treasuresCount;
    }

    public void setTreasuresCount(int treasuresCount) {
        this.treasuresCount = treasuresCount;
    }
}
