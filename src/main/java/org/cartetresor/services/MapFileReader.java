package org.cartetresor.services;

import org.cartetresor.models.GameData;

public interface MapFileReader {
    GameData getMap() throws RuntimeException;
}
