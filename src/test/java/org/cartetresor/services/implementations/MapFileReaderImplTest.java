package org.cartetresor.services.implementations;

import org.cartetresor.models.ExplorerDirection;
import org.cartetresor.models.GameData;
import org.cartetresor.models.LineData;
import org.cartetresor.models.MapCell;
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

class MapFileReaderImplTest {

    private MapFileReaderImpl test;

    @BeforeEach
    void setUp() {
        test = Mockito.spy(new MapFileReaderImpl());
    }

    @ParameterizedTest
    @CsvSource({"#, 0, 0, 0, 0", "C, 1, 0, 0, 0", "M, 0, 1, 0, 0", "T, 0, 0, 1, 0", "A, 0, 0, 0, 1"})
    void readRow_mapLine(String line, int verifyMapRowCalls, int verifyMountainRowCalls, int verifyTreasureRowCalls, int verifyExplorerRowCalls) {
        final var gameData = new GameData();

        doNothing().when(test).readMapRow(any(), any());
        doNothing().when(test).readMountainRow(any(), any());
        doNothing().when(test).readTreasureRow(any(), any());
        doNothing().when(test).readExplorerRow(any(), any());

        test.readRow(gameData, line);

        verify(test, times(verifyMapRowCalls)).readMapRow(any(), any());
        verify(test, times(verifyMountainRowCalls)).readMountainRow(any(), any());
        verify(test, times(verifyTreasureRowCalls)).readTreasureRow(any(), any());
        verify(test, times(verifyExplorerRowCalls)).readExplorerRow(any(), any());
    }

    @Test
    void readRow_invalidLine() {
        final var gameData = new GameData();
        final var line = "A - 3 - 4";

        doNothing().when(test).readMapRow(any(), any());

        assertThrows(IllegalArgumentException.class, () -> test.readRow(gameData, line));

        verify(test, never()).readMapRow(any(), any());
        verify(test, never()).readMountainRow(any(), any());
    }

    @Test
    void readMapRow() {
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "C - 1 - 2";

        doNothing().when(test).checkLineFormat(any(), any(), anyInt());

        test.readMapRow(treasureMap, line);

        assertEquals(1, treasureMap.size());
        assertEquals(2, treasureMap.getFirst().size());
    }

    @Test
    void getSafeLineData() {
        final var lineData = new LineData(0, 0, 0);
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "T - 0 - 0";

        doNothing().when(test).checkLineFormat(any(), any(), anyInt());
        doNothing().when(test).checkCoordinates(anyInt(), anyInt(), any());

        final var result = test.getSafeLineData(treasureMap, line, 3);

        assertEquals(lineData, result);
        verify(test).checkLineFormat(any(), eq(line), eq(3));
        verify(test).checkCoordinates(0, 0, treasureMap);
    }

    @Test
    void getSafeLineData_fourthValue() {
        final var lineData = new LineData(0, 0, 1);
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "T - 0 - 0 - 1";

        doNothing().when(test).checkLineFormat(any(), any(), anyInt());
        doNothing().when(test).checkCoordinates(anyInt(), anyInt(), any());

        final var result = test.getSafeLineData(treasureMap, line, 3);

        assertEquals(lineData, result);
        verify(test).checkLineFormat(any(), eq(line), eq(3));
        verify(test).checkCoordinates(0, 0, treasureMap);
    }

    @Test
    void readMountainRow() {
        final var lineData = new LineData(0, 0, 0);
        final var mapCell = new MapCell();
        final var mapRow = List.of(mapCell);
        final var treasureMap = List.of(mapRow);
        final var line = "M - 0 - 0";

        doReturn(lineData).when(test).getSafeLineData(any(), any(), anyInt());

        test.readMountainRow(treasureMap, line);

        assertTrue(mapCell.isMountain());
    }

    @Test
    void readTreasureRow() {
        final var lineData = new LineData(0, 0, 1);
        final var mapCell = new MapCell();
        final var mapRow = List.of(mapCell);
        final var treasureMap = List.of(mapRow);
        final var line = "T - 0 - 0 - 1";

        doReturn(lineData).when(test).getSafeLineData(any(), any(), anyInt());

        test.readTreasureRow(treasureMap, line);

        assertEquals(1, mapCell.getTreasuresCount());
    }

    @ParameterizedTest
    @CsvSource({"N,NORTH", "S,SOUTH", "E,EAST", "W,WEST"})
    void parseDirection(String input, ExplorerDirection expected) {
        final var result = test.parseDirection(input);
        assertEquals(expected, result);
    }

    @Test
    void parseDirection_invalid() {
        assertThrows(IllegalArgumentException.class, () -> test.parseDirection("X"));
    }

    @Test
    void readExplorerRow() {
        final var gameData = new GameData();
        final var line = "A - Lara - 0 - 0 - N - AADADAGGA";

        doNothing().when(test).checkLineFormat(any(), any(), anyInt());
        doNothing().when(test).checkCoordinates(anyInt(), anyInt(), any());

        test.readExplorerRow(gameData, line);

        assertEquals(1, gameData.getExplorers().size());
        final var explorer = gameData.getExplorers().getFirst();
        assertEquals("Lara", explorer.getName());
        assertEquals(ExplorerDirection.NORTH, explorer.getDirection());
        assertEquals(0, explorer.getX());
        assertEquals(0, explorer.getY());
        assertEquals(List.of("A", "A", "D", "A", "D", "A", "G", "G", "A"), explorer.getActionSequence());
    }

    @Test
    void checkLineFormat_success() {
        assertDoesNotThrow(() -> test.checkLineFormat(new String[]{"C", "3", "4"}, "C - 3 - 4", 3));
    }

    @Test
    void checkLineFormat_error() {
        assertThrows(IllegalArgumentException.class, () -> test.checkLineFormat(new String[]{"C", "3"}, "C - 3", 3));
    }
}