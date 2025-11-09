package org.cartetresor.services.implementations;

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
    public void generateMapFile(List<List<MapCell>> treasureMap) throws RuntimeException {
        final var lines = new ArrayList<String>();
        lines.add(generateMapLine(treasureMap));
        lines.addAll(generateMountainLine(treasureMap));
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

    List<String> generateMountainLine(List<List<MapCell>> treasureMap) {
        final var mountainLines = new ArrayList<String>();
        for (int y = 0; y < treasureMap.size(); y++) {
            for (int x = 0; x < treasureMap.get(y).size(); x++) {
                final var cell = treasureMap.get(y).get(x);
                if (cell.isMountain()) {
                    mountainLines.add("M - " + x + " - " + y);
                }
            }
        }
        return mountainLines;
    }
}
