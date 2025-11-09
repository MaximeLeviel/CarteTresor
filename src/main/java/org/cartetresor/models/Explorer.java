package org.cartetresor.models;

import java.util.List;

public class Explorer {
    private final String name;
    private ExplorerDirection direction;
    private int coordX;
    private int coordY;
    private int treasuresCollected = 0;
    private List<String> actionSequence;

    public Explorer(String name, ExplorerDirection direction, int coordX, int coordY, List<String> actionSequence) {
        this.name = name;
        this.direction = direction;
        this.coordX = coordX;
        this.coordY = coordY;
        this.actionSequence = actionSequence;
    }

    public String getName() {
        return name;
    }

    public ExplorerDirection getDirection() {
        return direction;
    }

    public void setDirection(ExplorerDirection direction) {
        this.direction = direction;
    }

    public int getCoordX() {
        return coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public int getTreasuresCollected() {
        return treasuresCollected;
    }

    public void setTreasuresCollected(int treasuresCollected) {
        this.treasuresCollected = treasuresCollected;
    }

    public List<String> getActionSequence() {
        return actionSequence;
    }

    public void setActionSequence(List<String> actionSequence) {
        this.actionSequence = actionSequence;
    }
}
