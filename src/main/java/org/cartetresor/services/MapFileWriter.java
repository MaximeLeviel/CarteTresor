package org.cartetresor.services;

import org.cartetresor.models.MapCell;

import java.util.List;

public interface MapFileWriter {
    void generateMapFile(List<List<MapCell>> treasureMap);
}
