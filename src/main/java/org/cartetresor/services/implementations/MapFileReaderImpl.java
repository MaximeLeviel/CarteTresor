package org.cartetresor.services.implementations;

import org.cartetresor.models.LineData;
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
        } catch (IOException e) {
            throw new RuntimeException("File was not found: " + e.getMessage(), e);
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
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file: " + e.getMessage(), e);
        } catch (IllegalArgumentException e){
            throw new RuntimeException("Error while mapping file: " + e.getMessage(), e);
        }
    }

    void readRow(List<List<MapCell>> treasureMap, String line) throws IllegalArgumentException {
        switch (line) {
            case String s when s.startsWith("#") -> {
                //Comment line, do nothing
            }
            case String s when s.startsWith("C") -> readMapRow(treasureMap, line);
            case String s when s.startsWith("M") -> readMountainRow(treasureMap, line);
            case String s when s.startsWith("T") -> readTreasureRow(treasureMap, line);
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
        final var nbOfTreasures = values.length > 3 ? Integer.parseInt(values[3].strip()) : 0;
        checkCoordinates(coordX, coordY, treasureMap);
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
