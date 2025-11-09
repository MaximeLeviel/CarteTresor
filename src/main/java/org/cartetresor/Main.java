package org.cartetresor;

import org.cartetresor.services.implementations.MapFileReaderImpl;

public class Main {
    public static void main(String[] args) {
        final var fileReader = new MapFileReaderImpl();
        final var result = fileReader.getMap();

        System.out.println("Map size: " + result.size() + " x " + (result.isEmpty() ? 0 : result.getFirst().size()));
    }
}