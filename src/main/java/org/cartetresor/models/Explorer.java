package org.cartetresor.models;

import java.util.List;

public class Explorer {
    private final String name;
    private ExplorerDirection direction;
    private int x;
    private int y;
    private List<String> actionSequence;

    public Explorer(String name, ExplorerDirection direction, int x, int y, List<String> actionSequence) {
        this.name = name;
        this.direction = direction;
        this.x = x;
        this.y = y;
        this.actionSequence = actionSequence;
    }

    public String getName() {
        return name;
    }

    public ExplorerDirection getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<String> getActionSequence() {
        return actionSequence;
    }
}
