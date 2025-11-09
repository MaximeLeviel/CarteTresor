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
        writeMap(lines);
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
}
