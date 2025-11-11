package org.cartetresor.services.implementations;

import org.cartetresor.models.*;
import org.cartetresor.services.MapFileReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFileReaderImpl implements MapFileReader {

    @Override
    public SimulationData getMap() throws RuntimeException {
        try (BufferedReader mapFile = new BufferedReader(new FileReader("src/main/resources/mapFile.txt"))) {
            return readFile(mapFile);
        } catch (IOException e) {
            throw new RuntimeException("File was not found: " + e.getMessage(), e);
        }
    }

    public SimulationData readFile(BufferedReader mapFile) throws RuntimeException {
        final var simulationData = new SimulationData();
        try {
            var line = mapFile.readLine();
            while (line != null) {
                readRow(simulationData, line);
                line = mapFile.readLine();
            }

            return simulationData;
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file: " + e.getMessage(), e);
        } catch (IllegalArgumentException e){
            throw new RuntimeException("Error while mapping file: " + e.getMessage(), e);
        }
    }

    void readRow(SimulationData simulationData, String line) throws IllegalArgumentException {
        switch (line) {
            case String s when s.startsWith("#") -> {
                //Comment line, do nothing
            }
            case String s when s.startsWith("C") -> readMapRow(simulationData.getTreasureMap(), line);
            case String s when s.startsWith("M") -> readMountainRow(simulationData.getTreasureMap(), line);
            case String s when s.startsWith("T") -> readTreasureRow(simulationData.getTreasureMap(), line);
            case String s when s.startsWith("A") -> readExplorerRow(simulationData, line);
            default -> throw new IllegalArgumentException("Unknown line type: " + line);
        }
    }

    void readMapRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        final var values = line.split(" - ");
        checkLineFormat(values, line, 3);
        for (var i = 0; i < Integer.parseInt(values[1].strip()); i++) {
            final var column = new ArrayList<MapCell>();
            for (var j = 0; j < Integer.parseInt(values[2].strip()); j++) {
                column.add(new MapCell());
            }
            treasureMap.add(column);
        }
    }

    LineData getSafeLineData(List<List<MapCell>> treasureMap, String line, int expectedSize) {
        final var values = line.split(" - ");
        checkLineFormat(values, line, expectedSize);
        final var coordX = Integer.parseInt(values[1].strip());
        final var coordY = Integer.parseInt(values[2].strip());
        checkCoordinates(coordX, coordY, treasureMap);
        final var nbOfTreasures = values.length > 3 ? Integer.parseInt(values[3].strip()) : 0;
        return new LineData(coordX, coordY, nbOfTreasures);
    }

    void readMountainRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        final var lineData = getSafeLineData(treasureMap, line, 3);
        final var mapCell = treasureMap.get(lineData.getX()).get(lineData.getY());
        mapCell.setMountain(true);
    }

    void readTreasureRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        final var lineData = getSafeLineData(treasureMap, line, 4);
        final var mapCell = treasureMap.get(lineData.getX()).get(lineData.getY());
        mapCell.setTreasuresCount(lineData.getNbOfTreasures());
    }

    void readExplorerRow(SimulationData simulationData, String line) throws IllegalArgumentException {
        final var values = line.split(" - ");
        checkLineFormat(values, line, 6);
        final var coordX = Integer.parseInt(values[2].strip());
        final var coordY = Integer.parseInt(values[3].strip());
        checkCoordinates(coordX, coordY, simulationData.getTreasureMap());
        final var name = values[1].strip();
        final var direction = values[4].strip();
        final var formattedDirection = ExplorerDirection.valueOfLabel(direction);
        if (formattedDirection == null) {
            throw new IllegalArgumentException("Invalid explorer direction: " + direction);
        }
        final var actionSequence = new ArrayList<>(Arrays.asList(values[5].strip().split("")));
        final var newExplorer = new Explorer(name, formattedDirection, coordX, coordY, actionSequence);
        simulationData.getExplorers().add(newExplorer);
    }

    void checkLineFormat(String[] values, String line, int expectedSize) throws IllegalArgumentException {
        if (values.length != expectedSize) {
            throw new IllegalArgumentException("Map line is not valid: " + line);
        }
    }

    void checkCoordinates(int x, int y, List<List<MapCell>> treasureMap) throws IllegalArgumentException {
        if (x < 0 || x >= treasureMap.size() || y < 0 || y >= treasureMap.getFirst().size()) {
            throw new IllegalArgumentException("Coordinates are out of map bounds: (" + x + ", " + y + ")");
        }
    }
}
