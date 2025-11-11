package org.cartetresor.services.implementations;

import org.cartetresor.models.Explorer;
import org.cartetresor.models.SimulationData;
import org.cartetresor.models.MapCell;
import org.cartetresor.services.MapFileWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MapFileWriterImpl implements MapFileWriter {

    @Override
    public void generateMapFile(SimulationData simulationData) throws RuntimeException {
        final var lines = new ArrayList<String>();
        lines.add(generateMapLine(simulationData.getTreasureMap()));
        lines.addAll(generateMountainLines(simulationData.getTreasureMap()));
        lines.addAll(generateTreasureLines(simulationData.getTreasureMap()));
        lines.addAll(generateExplorersLines(simulationData.getExplorers()));
        displayLines(lines);
        writeMap(lines);
    }

    void displayLines(List<String> lines) {
        for (var line : lines) {
            System.out.println(line);
        }
    }

    void writeMap(List<String> lines) throws RuntimeException {
        final var file = Paths.get("mapOutput.txt");
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing file", e);
        }
    }

    String generateMapLine(List<List<MapCell>> treasureMap) {
        return "C - " + treasureMap.size() + " - " + treasureMap.getFirst().size();
    }

    List<String> generateMountainLines(List<List<MapCell>> treasureMap) {
        final var mountainLines = new ArrayList<String>();
        for (int x = 0; x < treasureMap.size(); x++) {
            for (int y = 0; y < treasureMap.get(x).size(); y++) {
                final var cell = treasureMap.get(x).get(y);
                if (cell.isMountain()) {
                    mountainLines.add("M - " + x + " - " + y);
                }
            }
        }
        return mountainLines;
    }

    List<String> generateTreasureLines(List<List<MapCell>> treasureMap) {
        final var treasureLines = new ArrayList<String>();
        for (int x = 0; x < treasureMap.size(); x++) {
            for (int y = 0; y < treasureMap.get(x).size(); y++) {
                final var cell = treasureMap.get(x).get(y);
                if (cell.getTreasuresCount() > 0) {
                    treasureLines.add("T - " + x + " - " + y + " - " + cell.getTreasuresCount());
                }
            }
        }
        if (treasureLines.isEmpty()) {
            treasureLines.addFirst("# Aucun trésor restant sur la carte");
        } else {
            treasureLines.addFirst("# {T comme Trésor} - {Axe horizontal} - {Axe vertical} - {Nb. de trésors restants}"); // Remove header if no treasures
        }
        return treasureLines;
    }

    List<String> generateExplorersLines(List<Explorer> explorers) {
        final var explorerLines = new ArrayList<String>();
        for (var explorer : explorers) {
            explorerLines.add("A - " + explorer.getName() + " - " + explorer.getCoordX() + " - " + explorer.getCoordY() + " - " + explorer.getDirection().getShortName() + " - " + explorer.getTreasuresCollected());
        }
        if (explorers.isEmpty()) {
            explorerLines.addFirst("# Aucun aventurier n'a exploré la carte");
        } else {
            explorerLines.addFirst("# {A comme Aventurier} - {Nom} - {Axe horizontal} - {Axe vertical} - {Orientation} - {Nb. de trésors ramassés}"); // Remove header if no explorers
        }
        return explorerLines;
    }
}
