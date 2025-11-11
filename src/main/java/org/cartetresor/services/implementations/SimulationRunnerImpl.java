package org.cartetresor.services.implementations;

import org.cartetresor.models.Explorer;
import org.cartetresor.models.ExplorerDirection;
import org.cartetresor.models.MapCell;
import org.cartetresor.models.SimulationData;
import org.cartetresor.services.SimulationRunner;

import java.util.List;

public class SimulationRunnerImpl implements SimulationRunner {

    @Override
    public void run(SimulationData simulationData) {
        if (simulationData.getExplorers().isEmpty()) {
            return;
        }
        final var nbOfActions = simulationData.getExplorers().getFirst().getActionSequence().size();
        for (var i = 0; i < nbOfActions; i++) {
            for (var explorer : simulationData.getExplorers()) {
               act(simulationData.getTreasureMap(), explorer, simulationData.getExplorers());
               explorer.getActionSequence().removeFirst();
            }
        }
    }

    void act(List<List<MapCell>> treasureMap, Explorer explorer, List<Explorer> explorers) {
        switch (explorer.getActionSequence().getFirst()) {
            case "A" -> moveToDirection(treasureMap, explorer, explorers);
            case "G" -> turn(explorer, true);
            case "D" -> turn(explorer, false);
        }
    }

    void moveToDirection(List<List<MapCell>> treasureMap, Explorer explorer, List<Explorer> explorers) {
        switch(explorer.getDirection()) {
            case ExplorerDirection.NORTH -> move(treasureMap, explorer, explorers, explorer.getCoordX(), explorer.getCoordY() - 1);
            case ExplorerDirection.SOUTH -> move(treasureMap, explorer, explorers, explorer.getCoordX(), explorer.getCoordY() + 1);
            case ExplorerDirection.EAST -> move(treasureMap, explorer, explorers, explorer.getCoordX() + 1, explorer.getCoordY());
            case ExplorerDirection.WEST -> move(treasureMap, explorer, explorers, explorer.getCoordX() - 1, explorer.getCoordY());
        }
    }

    void move(List<List<MapCell>> treasureMap, Explorer explorer, List<Explorer> explorers, int newX, int newY) {
        if (canMoveTo(treasureMap, explorers, newX, newY)) {
            explorer.setCoordX(newX);
            explorer.setCoordY(newY);
            collectTreasure(treasureMap, explorer);
        }
    }

    void collectTreasure(List<List<MapCell>> treasureMap, Explorer explorer) {
        final var cell = treasureMap.get(explorer.getCoordX()).get(explorer.getCoordY());
        if (cell.getTreasuresCount() > 0) {
            cell.setTreasuresCount(cell.getTreasuresCount() - 1);
            explorer.setTreasuresCollected(explorer.getTreasuresCollected() + 1);
        }
    }

    boolean canMoveTo(List<List<MapCell>> treasureMap, List<Explorer> explorers, int newX, int newY) {
        return newX >= 0 && newX < treasureMap.size() &&
               newY >= 0 && newY < treasureMap.getFirst().size() &&
               !treasureMap.get(newX).get(newY).isMountain() &&
               explorers.stream().noneMatch(explorer -> explorer.getCoordX() == newX && explorer.getCoordY() == newY);
    }

    void turn(Explorer explorer, boolean left) {
        switch (explorer.getDirection()) {
            case ExplorerDirection.NORTH -> explorer.setDirection(left ? ExplorerDirection.WEST : ExplorerDirection.EAST);
            case ExplorerDirection.SOUTH -> explorer.setDirection(left ? ExplorerDirection.EAST : ExplorerDirection.WEST);
            case ExplorerDirection.EAST -> explorer.setDirection(left ? ExplorerDirection.NORTH : ExplorerDirection.SOUTH);
            case ExplorerDirection.WEST -> explorer.setDirection(left ? ExplorerDirection.SOUTH : ExplorerDirection.NORTH);
        }
    }
}
