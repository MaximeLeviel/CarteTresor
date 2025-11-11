package org.cartetresor.services.implementations;

import org.cartetresor.models.Explorer;
import org.cartetresor.models.ExplorerDirection;
import org.cartetresor.models.MapCell;
import org.cartetresor.models.SimulationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SimulationRunnerImplTest {

    private SimulationRunnerImpl test;

    @BeforeEach
    void setUp() {
        test = Mockito.spy(new SimulationRunnerImpl());
    }

    @Test
    void run_noExplorer() {
        final var simulationData = new SimulationData();

        test.run(simulationData);

        verify(test, never()).act(any(), any(), any());
    }

    @Test
    void run() {
        final var actionSequence = new ArrayList<>(List.of("A"));
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 1, 1, actionSequence);
        final var simulationData = new SimulationData();
        simulationData.getExplorers().add(explorer);

        doNothing().when(test).act(any(), any(), any());

        test.run(simulationData);

        verify(test).act(List.of(), explorer, List.of(explorer));
    }

    @Test
    void act_move() {
        final var actionSequence = new ArrayList<>(List.of("A"));
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 1, 1, actionSequence);
        final var treasureMap = new ArrayList<List<MapCell>>();

        doNothing().when(test).moveToDirection(any(), any(), any());

        test.act(treasureMap, explorer, List.of());

        verify(test).moveToDirection(treasureMap, explorer, List.of());
    }

    @Test
    void act_turnLeft() {
        final var actionSequence = new ArrayList<>(List.of("G"));
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 1, 1, actionSequence);
        final var treasureMap = new ArrayList<List<MapCell>>();

        doNothing().when(test).turn(any(), anyBoolean());

        test.act(treasureMap, explorer, List.of());

        verify(test).turn(explorer, true);
    }

    @Test
    void act_turnRight() {
        final var actionSequence = new ArrayList<>(List.of("D"));
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 1, 1, actionSequence);
        final var treasureMap = new ArrayList<List<MapCell>>();

        doNothing().when(test).turn(any(), anyBoolean());

        test.act(treasureMap, explorer, List.of());

        verify(test).turn(explorer, false);
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, 0, -1",
            "SOUTH, 0, 1",
            "EAST, 1, 0",
            "WEST, -1, 0"
    })
    void moveToDirection(ExplorerDirection direction, int newX, int newY) {
        final var explorer = new Explorer("explorer", direction, 0, 0, List.of());
        final var treasureMap = new ArrayList<List<MapCell>>();

        doNothing().when(test).move(any(), any(), any(), anyInt(), anyInt());

        test.moveToDirection(treasureMap, explorer, List.of());

        verify(test).move(treasureMap, explorer, List.of(), newX, newY);
    }

    @Test
    void move_canMove() {
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 0, 0, List.of());
        final var treasureMap = new ArrayList<List<MapCell>>();

        doReturn(true).when(test).canMoveTo(any(), any(), anyInt(), anyInt());
        doNothing().when(test).collectTreasure(any(), any());

        test.move(treasureMap, explorer, List.of(), 1, 1);

        assertEquals(1, explorer.getCoordX());
        assertEquals(1, explorer.getCoordY());
        verify(test).canMoveTo(treasureMap, List.of(), 1, 1);
        verify(test).collectTreasure(treasureMap, explorer);
    }

    @Test
    void move_cantMove() {
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 0, 0, List.of());
        final var treasureMap = new ArrayList<List<MapCell>>();

        doReturn(false).when(test).canMoveTo(any(), any(), anyInt(), anyInt());

        test.move(treasureMap, explorer, List.of(), 1, 1);

        assertEquals(0, explorer.getCoordX());
        assertEquals(0, explorer.getCoordY());
        verify(test).canMoveTo(treasureMap, List.of(), 1, 1);
        verify(test, never()).collectTreasure(any(), any());
    }

    @Test
    void collectTreasure() {
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 0, 0, List.of());
        final var mapCell = new MapCell();
        mapCell.setTreasuresCount(1);
        final var treasureMap = List.of(List.of(mapCell));

        test.collectTreasure(treasureMap, explorer);

        assertEquals(1, explorer.getTreasuresCollected());
        assertEquals(0, mapCell.getTreasuresCount());
    }

    @Test
    void collectTreasure_noTreasure() {
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, 0, 0, List.of());
        final var mapCell = new MapCell();
        final var treasureMap = List.of(List.of(mapCell));

        test.collectTreasure(treasureMap, explorer);

        assertEquals(0, explorer.getTreasuresCollected());
        assertEquals(0, mapCell.getTreasuresCount());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, true",
            "-1, 0, false",
            "0, -1, false",
            "10, 0, false",
            "0, 10, false"
    })
    void canMoveTo(int newX, int newY, boolean expected) {
        final var mapCell = new MapCell();
        final var treasureMap = List.of(List.of(mapCell));

        final var result = test.canMoveTo(treasureMap, List.of(), newX, newY);

        assertEquals(expected, result);
    }

    @Test
    void canMoveTo_mountain() {
        final var mapCell = new MapCell();
        mapCell.setMountain(true);
        final var treasureMap = List.of(List.of(mapCell));

        final var result = test.canMoveTo(treasureMap, List.of(), 0, 0);

        assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, false",
            "0, 1, true",
            "1, 1, true"
    })
    void canMoveTo_otherExplorer(int explorerX, int explorerY, boolean expected) {
        final var mapCell = new MapCell();
        final var treasureMap = List.of(List.of(mapCell));
        final var explorer = new Explorer("explorer", ExplorerDirection.SOUTH, explorerX, explorerY, List.of());

        final var result = test.canMoveTo(treasureMap, List.of(explorer), 0, 0);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, true, WEST",
            "NORTH, false, EAST",
            "SOUTH, true, EAST",
            "SOUTH, false, WEST",
            "EAST, true, NORTH",
            "EAST, false, SOUTH",
            "WEST, true, SOUTH",
            "WEST, false, NORTH"
    })
    void turn(ExplorerDirection initialDirection, boolean left, ExplorerDirection expectedDirection) {
        final var explorer = new Explorer("explorer", initialDirection, 0, 0, List.of());

        test.turn(explorer, left);

        assertEquals(expectedDirection, explorer.getDirection());
    }
}