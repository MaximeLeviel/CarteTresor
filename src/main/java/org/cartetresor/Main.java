package org.cartetresor;

import org.cartetresor.services.implementations.MapFileReaderImpl;
import org.cartetresor.services.implementations.MapFileWriterImpl;

public class Main {
    public static void main(String[] args) {
        try {
            final var fileReader = new MapFileReaderImpl();
            final var gameData = fileReader.getMap();

            try {
                final var fileWriter = new MapFileWriterImpl();
                fileWriter.generateMapFile(gameData);
            } catch (RuntimeException e) {
                System.err.println("An error occurred while writing the map file: " + e.getMessage());
            }
        } catch (RuntimeException e) {
            System.err.println("An error occurred while reading the map file: " + e.getMessage());
        }
    }
}