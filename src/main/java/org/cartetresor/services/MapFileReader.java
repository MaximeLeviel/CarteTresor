package org.cartetresor.services;

import org.cartetresor.models.MapCell;

import java.util.List;

public interface MapFileReader {
    List<List<MapCell>> getMap() throws RuntimeException;
}
