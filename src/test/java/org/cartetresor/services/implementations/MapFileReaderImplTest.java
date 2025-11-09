package org.cartetresor.services.implementations;

import org.cartetresor.models.MapCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void readRow_mapLine() {
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "C - 3 - 4";

        doNothing().when(test).readMountainRow(any(), any());

        test.readRow(treasureMap, line);

        verify(test).readMapRow(treasureMap, line);
        verify(test, never()).readMountainRow(any(), any());
    }

    @Test
    void readRow_mountainLine() {
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "M - 3 - 4";

        doNothing().when(test).readMountainRow(any(), any());

        test.readRow(treasureMap, line);

        verify(test, never()).readMapRow(any(), any());
        verify(test).readMountainRow(treasureMap, line);
    }

    @Test
    void readRow_nonMapLine() {
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "A - 3 - 4";

        doNothing().when(test).readMapRow(any(), any());

        test.readRow(treasureMap, line);

        verify(test, never()).readMapRow(any(), any());
    }

    @Test
    void readMapRow() {
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var line = "C - 1 - 2";

        doNothing().when(test).checkLineFormat(any(), any());

        test.readMapRow(treasureMap, line);

        assertEquals(1, treasureMap.size());
        assertEquals(2, treasureMap.getFirst().size());
    }

    @Test
    void readMountainRow() {
        final var mapCell = new MapCell();
        final var mapRow = List.of(mapCell);
        final var treasureMap = List.of(mapRow);
        final var line = "M - 0 - 0";

        doNothing().when(test).checkLineFormat(any(), any());
        doNothing().when(test).checkCoordinates(anyInt(), anyInt(), any());

        test.readMountainRow(treasureMap, line);

        assertTrue(mapCell.isMountain());
    }

    @Test
    void checkLineFormat_success() {
        assertDoesNotThrow(() -> test.checkLineFormat(new String[]{"C", "3", "4"}, "C - 3 - 4"));
    }

    @Test
    void checkLineFormat_error() {
        assertThrows(IllegalArgumentException.class, () -> test.checkLineFormat(new String[]{"C", "3"}, "C - 3"));
    }
}