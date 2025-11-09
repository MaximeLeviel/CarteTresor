package org.cartetresor.models;

public enum ExplorerDirection {
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private final String shortName;

    ExplorerDirection(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static ExplorerDirection valueOfLabel(String label) {
        for (var e : values()) {
            if (e.shortName.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
