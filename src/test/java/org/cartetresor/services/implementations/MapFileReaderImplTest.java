package org.cartetresor.services.implementations;

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
    @CsvSource({"#, 0, 0, 0", "C, 1, 0, 0", "M, 0, 1, 0", "T, 0, 0, 1"})
    void readRow_mapLine(String line, int verifyMapRowCalls, int verifyMountainRowCalls, int verifyTreasureRowCalls) {
        final var treasureMap = new ArrayList<List<MapCell>>();

        doNothing().when(test).readMapRow(any(), any());
        doNothing().when(test).readMountainRow(any(), any());
        doNothing().when(test).readTreasureRow(any(), any());

        test.readRow(treasureMap, line);

        verify(test, times(verifyMapRowCalls)).readMapRow(treasureMap, line);
        verify(test, times(verifyMountainRowCalls)).readMountainRow(any(), any());
        verify(test, times(verifyTreasureRowCalls)).readTreasureRow(any(), any());
    }

    @Test
    void readRow_invalidLine() {
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "A - 3 - 4";

        doNothing().when(test).readMapRow(any(), any());

        assertThrows(IllegalArgumentException.class, () -> test.readRow(treasureMap, line));

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

    @Test
    void checkLineFormat_success() {
        assertDoesNotThrow(() -> test.checkLineFormat(new String[]{"C", "3", "4"}, "C - 3 - 4", 3));
    }

    @Test
    void checkLineFormat_error() {
        assertThrows(IllegalArgumentException.class, () -> test.checkLineFormat(new String[]{"C", "3"}, "C - 3", 3));
    }
}