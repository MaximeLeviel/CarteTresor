package org.cartetresor.services.implementations;

import org.cartetresor.models.Explorer;
import org.cartetresor.models.ExplorerDirection;
import org.cartetresor.models.SimulationData;
import org.cartetresor.models.MapCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MapFileWriterImplTest {

    private MapFileWriterImpl test;

    @BeforeEach
    void setUp() {
        test = Mockito.spy(new MapFileWriterImpl());
    }

    @Test
    void generateMapFile() {
        final var simulationData = new SimulationData();
        final var mapLine = "mapLine";
        final var mountainLines = List.of("mountainLine");
        final var treasureLines = List.of("treasureLine");
        final var explorerLines = List.of("explorerLine");
        final var lines = List.of("mapLine", "mountainLine", "treasureLine", "explorerLine");

        doReturn(mapLine).when(test).generateMapLine(any());
        doReturn(mountainLines).when(test).generateMountainLines(any());
        doReturn(treasureLines).when(test).generateTreasureLines(any());
        doReturn(explorerLines).when(test).generateExplorersLines(any());
        doNothing().when(test).displayLines(any());
        doNothing().when(test).writeMap(any());

        test.generateMapFile(simulationData);

        verify(test).generateMapLine(simulationData.getTreasureMap());
        verify(test).generateMountainLines(simulationData.getTreasureMap());
        verify(test).generateExplorersLines(simulationData.getExplorers());
        verify(test).displayLines(lines);
        verify(test).writeMap(lines);
    }

    @Test
    void writeMap() {
        final var lines = List.of("line1", "line2");

        try (var mockPaths = mockStatic(Paths.class);
             var mockFiles = mockStatic(Files.class)) {
            final var mockFile = mock(Path.class);
            mockPaths.when(() -> Paths.get("mapOutput.txt")).thenReturn(mockFile);
            mockFiles.when(() -> Files.write(mockFile, lines, StandardCharsets.UTF_8) ).thenReturn(null);

            assertDoesNotThrow(() -> test.writeMap(lines));

            mockPaths.verify(() -> Paths.get("mapOutput.txt"));
            mockFiles.verify(() -> Files.write(mockFile, lines, StandardCharsets.UTF_8));
        }
    }

    @Test
    void writeMap_error() {
        final var lines = List.of("line1", "line2");

        try (var mockPaths = mockStatic(Paths.class);
             var mockFiles = mockStatic(Files.class)) {
            final var mockFile = mock(Path.class);
            mockPaths.when(() -> Paths.get("mapOutput.txt")).thenReturn(mockFile);
            mockFiles.when(() -> Files.write(mockFile, lines, StandardCharsets.UTF_8) ).thenThrow(IOException.class);

            assertThrows(RuntimeException.class, () -> test.writeMap(lines));

            mockPaths.verify(() -> Paths.get("mapOutput.txt"));
            mockFiles.verify(() -> Files.write(mockFile, lines, StandardCharsets.UTF_8));
        }
    }

    @Test
    void generateMapLine() {
        final var cell1 = new MapCell();
        final var cell2 = new MapCell();
        final var row1 = List.of(cell1, cell2);
        final var treasureMap = List.of(row1);

        final var result = test.generateMapLine(treasureMap);

        assertEquals("C - 1 - 2", result);
    }

    @Test
    void generateMountainLines() {
        final var cell1 = new MapCell();
        final var cell2 = new MapCell();
        cell2.setMountain(true);
        final var cell3 = new MapCell();
        cell3.setMountain(true);
        final var cell4 = new MapCell();
        final var row1 = List.of(cell1, cell2);
        final var row2 = List.of(cell3, cell4);
        final var treasureMap = List.of(row1, row2);

        final var result = test.generateMountainLines(treasureMap);

        assertEquals(2, result.size());
        assertEquals("M - 0 - 1", result.get(0));
        assertEquals("M - 1 - 0", result.get(1));
    }

    @Test
    void generateTreasureLines() {
        final var cell1 = new MapCell();
        final var cell2 = new MapCell();
        cell2.setTreasuresCount(1);
        final var cell3 = new MapCell();
        cell3.setTreasuresCount(2);
        final var cell4 = new MapCell();
        final var row1 = List.of(cell1, cell2);
        final var row2 = List.of(cell3, cell4);
        final var treasureMap = List.of(row1, row2);

        final var result = test.generateTreasureLines(treasureMap);

        assertEquals(3, result.size());
        assertEquals("T - 0 - 1 - 1", result.get(1));
        assertEquals("T - 1 - 0 - 2", result.get(2));
    }

    @Test
    void generateTreasureLines_empty() {
        final var result = test.generateTreasureLines(List.of());

        assertEquals(1, result.size());
    }

    @Test
    void generateExplorersLines() {
        final var explorer1 = new Explorer("Lara", ExplorerDirection.SOUTH, 0, 0, new ArrayList<>());
        final var explorer2 = new Explorer("Indiana", ExplorerDirection.EAST, 1, 1, new ArrayList<>());
        final var explorers = List.of(explorer1, explorer2);

        final var result = test.generateExplorersLines(explorers);

        assertEquals(3, result.size());
        assertEquals("A - Lara - 0 - 0 - S - 0", result.get(1));
        assertEquals("A - Indiana - 1 - 1 - E - 0", result.get(2));
    }

    @Test
    void generateExplorersLines_empty() {
        final var result = test.generateExplorersLines(List.of());

        assertEquals(1, result.size());
    }
}
