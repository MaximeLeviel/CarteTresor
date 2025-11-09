package org.cartetresor;

import org.cartetresor.services.implementations.MapFileReaderImpl;
import org.cartetresor.services.implementations.MapFileWriterImpl;

public class Main {
    public static void main(String[] args) {
        final var fileReader = new MapFileReaderImpl();
        final var treasureMap = fileReader.getMap();

        final var fileWriter = new MapFileWriterImpl();
        fileWriter.generateMapFile(treasureMap);
    }
}