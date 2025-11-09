package org.cartetresor.models;

import java.util.ArrayList;
import java.util.List;

public class SimulationData {
    private final List<List<MapCell>> treasureMap = new ArrayList<>();
    private final List<Explorer> explorers = new ArrayList<>();

    public List<List<MapCell>> getTreasureMap() {
        return treasureMap;
    }

    public List<Explorer> getExplorers() {
        return explorers;
    }
}
