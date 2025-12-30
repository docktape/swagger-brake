package com.docktape.swagger.brake.cli;

import com.docktape.swagger.brake.cli.options.CliHelpException;
import com.docktape.swagger.brake.cli.options.CliOptionsProvider;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.runner.Options;
import com.docktape.swagger.brake.runner.Starter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Cli class focusing on validating all exit codes.
 *
 * <p>
 * Exit codes:
 * - 0: No breaking changes detected, successful execution
 * - 1: Breaking changes found
 * - 2: Error during execution
 * - 3: Help was invoked
 */
@ExtendWith(MockitoExtension.class)
class CliTest {
    @Mock
    private CliOptionsProvider optionsProvider;

    @InjectMocks
    private Cli cli;

    private Options mockOptions;

    @BeforeEach
    void setUp() {
        mockOptions = mock(Options.class);
    }

    /**
     * Test exit code 0: No breaking changes were detected and execution was successful.
     */
    @Test
    void testStartReturnsZeroWhenNoBreakingChangesFound() {
        // Given: Options provider returns valid options and Starter finds no breaking changes
        when(optionsProvider.provide()).thenReturn(mockOptions);

        try (MockedStatic<Starter> starterMock = mockStatic(Starter.class)) {
            starterMock.when(() -> Starter.start(any(Options.class)))
                    .thenReturn(Collections.emptyList());

            // When: start() is called
            int exitCode = cli.start();

            // Then: Exit code should be 0
            assertEquals(0, exitCode, "Exit code should be 0 when no breaking changes are found");
            verify(optionsProvider, times(1)).provide();
            starterMock.verify(() -> Starter.start(mockOptions), times(1));
        }
    }

    /**
     * Test exit code 1: Breaking changes were found.
     */
    @Test
    void testStartReturnsOneWhenBreakingChangesFound() {
        // Given: Options provider returns valid options and Starter finds breaking changes
        when(optionsProvider.provide()).thenReturn(mockOptions);

        Collection<BreakingChange> breakingChanges = new ArrayList<>();
        breakingChanges.add(mock(BreakingChange.class));

        try (MockedStatic<Starter> starterMock = mockStatic(Starter.class)) {
            starterMock.when(() -> Starter.start(any(Options.class)))
                    .thenReturn(breakingChanges);

            // When: start() is called
            int exitCode = cli.start();

            // Then: Exit code should be 1
            assertEquals(1, exitCode, "Exit code should be 1 when breaking changes are found");
            verify(optionsProvider, times(1)).provide();
            starterMock.verify(() -> Starter.start(mockOptions), times(1));
        }
    }

    /**
     * Test exit code 2: Error occurred during execution.
     */
    @Test
    void testStartReturnsTwoWhenExecutionErrorOccurs() {
        // Given: Options provider throws a runtime exception (not CliHelpException)
        when(optionsProvider.provide()).thenThrow(new RuntimeException("Execution error"));

        // When: start() is called
        int exitCode = cli.start();

        // Then: Exit code should be 2
        assertEquals(2, exitCode, "Exit code should be 2 when an execution error occurs");
        verify(optionsProvider, times(1)).provide();
    }

    /**
     * Test exit code 2: Error occurred during Starter execution.
     */
    @Test
    void testStartReturnsTwoWhenStarterThrowsException() {
        // Given: Options provider returns valid options but Starter throws an exception
        when(optionsProvider.provide()).thenReturn(mockOptions);

        try (MockedStatic<Starter> starterMock = mockStatic(Starter.class)) {
            starterMock.when(() -> Starter.start(any(Options.class)))
                    .thenThrow(new RuntimeException("Starter execution error"));

            // When: start() is called
            int exitCode = cli.start();

            // Then: Exit code should be 2
            assertEquals(2, exitCode, "Exit code should be 2 when Starter throws an exception");
            verify(optionsProvider, times(1)).provide();
            starterMock.verify(() -> Starter.start(mockOptions), times(1));
        }
    }

    /**
     * Test exit code 3: Help was invoked via CliHelpException.
     */
    @Test
    void testStartReturnsThreeWhenHelpIsInvoked() {
        // Given: Options provider throws CliHelpException
        when(optionsProvider.provide()).thenThrow(new CliHelpException("Help message"));

        // When: start() is called
        int exitCode = cli.start();

        // Then: Exit code should be 3
        assertEquals(3, exitCode, "Exit code should be 3 when help is invoked");
        verify(optionsProvider, times(1)).provide();
    }

    /**
     * Test exit code 0: Null breaking changes collection is treated as no breaking changes.
     */
    @Test
    void testStartReturnsZeroWhenBreakingChangesIsNull() {
        // Given: Options provider returns valid options and Starter returns null
        when(optionsProvider.provide()).thenReturn(mockOptions);

        try (MockedStatic<Starter> starterMock = mockStatic(Starter.class)) {
            starterMock.when(() -> Starter.start(any(Options.class)))
                    .thenReturn(null);

            // When: start() is called
            int exitCode = cli.start();

            // Then: Exit code should be 0 (null is treated as empty)
            assertEquals(0, exitCode, "Exit code should be 0 when breaking changes is null");
            verify(optionsProvider, times(1)).provide();
            starterMock.verify(() -> Starter.start(mockOptions), times(1));
        }
    }

    /**
     * Test exit code 1: Multiple breaking changes found.
     */
    @Test
    void testStartReturnsOneWhenMultipleBreakingChangesFound() {
        // Given: Options provider returns valid options and Starter finds multiple breaking changes
        when(optionsProvider.provide()).thenReturn(mockOptions);

        Collection<BreakingChange> breakingChanges = new ArrayList<>();
        breakingChanges.add(mock(BreakingChange.class));
        breakingChanges.add(mock(BreakingChange.class));
        breakingChanges.add(mock(BreakingChange.class));

        try (MockedStatic<Starter> starterMock = mockStatic(Starter.class)) {
            starterMock.when(() -> Starter.start(any(Options.class)))
                    .thenReturn(breakingChanges);

            // When: start() is called
            int exitCode = cli.start();

            // Then: Exit code should be 1
            assertEquals(1, exitCode, "Exit code should be 1 when multiple breaking changes are found");
            verify(optionsProvider, times(1)).provide();
            starterMock.verify(() -> Starter.start(mockOptions), times(1));
        }
    }
}
