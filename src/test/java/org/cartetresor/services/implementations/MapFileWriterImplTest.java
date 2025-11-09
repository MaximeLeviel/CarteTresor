package org.cartetresor.services.implementations;

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
        final var treasureMap = new ArrayList<List<MapCell>>();
        final var mapLine = "mapLine";

        doReturn(mapLine).when(test).generateMapLine(any());
        doNothing().when(test).writeMap(any());

        test.generateMapFile(treasureMap);

        verify(test).generateMapFile(treasureMap);
        verify(test).writeMap(List.of(mapLine));
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
}
