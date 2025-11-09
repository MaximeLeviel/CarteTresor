package org.cartetresor.services.implementations;

import org.cartetresor.models.MapCell;
import org.cartetresor.services.MapFileReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MapFileReaderImpl implements MapFileReader {

    @Override
    public List<List<MapCell>> getMap() throws RuntimeException {
        try (BufferedReader mapFile = new BufferedReader(new FileReader("src/main/resources/mapFile.txt"))) {
            return readFile(mapFile);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("File was not found", e);
        }
    }

    public List<List<MapCell>> readFile(BufferedReader mapFile) throws RuntimeException {
        final var treasureMap = new ArrayList<List<MapCell>>();
        try {
            var line = mapFile.readLine();
            while (line != null) {
                readRow(treasureMap, line);
                line = mapFile.readLine();
            }

            return treasureMap;
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Error while reading file", e);
        }
    }

    void readRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        if (line.startsWith("C")) {
            readMapRow(treasureMap, line);
        }
        if (line.startsWith("M")) {
            readMountainRow(treasureMap, line);
        }
    }

    void readMapRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        final var values = line.split(" - ");
        checkLineFormat(values, line);
        for (var i = 0; i < Integer.parseInt(values[1].strip()); i++) {
            final var row = new ArrayList<MapCell>();
            for (var j = 0; j < Integer.parseInt(values[2].strip()); j++) {
                row.add(new MapCell());
            }
            treasureMap.add(row);
        }
    }

    void readMountainRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        final var values = line.split(" - ");
        checkLineFormat(values, line);
        final var coordX = Integer.parseInt(values[1].strip());
        final var coordY = Integer.parseInt(values[2].strip());
        checkCoordinates(coordX, coordY, treasureMap);
        final var mapCell = treasureMap.get(coordY).get(coordX);
        mapCell.setMountain(true);
    }

    void checkLineFormat(String[] values, String line) throws IllegalArgumentException {
        if (values.length != 3) {
            throw new IllegalArgumentException("Map line is not valid: " + line);
        }
    }

    void checkCoordinates(int x, int y, List<List<MapCell>> treasureMap) throws IllegalArgumentException {
        if (y < 0 || y >= treasureMap.size() || x < 0 || x >= treasureMap.getFirst().size()) {
            throw new IllegalArgumentException("Coordinates are out of map bounds: (" + x + ", " + y + ")");
        }
    }
}
