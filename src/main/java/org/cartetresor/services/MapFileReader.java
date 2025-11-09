package org.cartetresor.services;

import org.cartetresor.models.SimulationData;

public interface MapFileReader {
    SimulationData getMap() throws RuntimeException;
}
