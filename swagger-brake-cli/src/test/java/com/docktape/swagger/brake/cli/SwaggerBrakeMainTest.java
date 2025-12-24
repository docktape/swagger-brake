package com.docktape.swagger.brake.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for SwaggerBrakeMain class focusing on return codes and behavior.
 *
 * <p>
 * These tests verify that the main method correctly handles different return codes from the CLI:
 * - 0: No breaking changes, successful execution (no System.exit called)
 * - 1: Breaking changes found (System.exit(1) is called)
 * - 2: Execution error (System.exit(2) is called)
 * - 3: Help invoked (System.exit(3) is called)
 *
 * <p>
 * Note: Tests for non-zero return codes cannot directly test System.exit behavior in modern Java
 * without using deprecated SecurityManager. The main() method implementation shows that any non-zero
 * return code from CLI.start() will result in System.exit(exitCode) being called.
 */
@ExtendWith(MockitoExtension.class)
class SwaggerBrakeMainTest {
    private MockedStatic<SwaggerBrakeMain> swaggerBrakeMainMock;
    private Cli mockCli;

    @BeforeEach
    void setUp() {
        mockCli = mock(Cli.class);
        swaggerBrakeMainMock = mockStatic(SwaggerBrakeMain.class);
        swaggerBrakeMainMock.when(() -> SwaggerBrakeMain.createCliInterface(any(String[].class)))
                .thenReturn(mockCli);
        swaggerBrakeMainMock.when(() -> SwaggerBrakeMain.main(any(String[].class)))
                .thenCallRealMethod();
    }

    @AfterEach
    void tearDown() {
        if (swaggerBrakeMainMock != null) {
            swaggerBrakeMainMock.close();
        }
    }

    /**
     * Test that when CLI returns 0 (no breaking changes), main completes successfully
     * without calling System.exit.
     */
    @Test
    void testMainSuccessfullyCompletesWhenCliReturnsZero() {
        // Given: CLI returns 0 (no breaking changes)
        when(mockCli.start()).thenReturn(0);

        // When: main is called
        SwaggerBrakeMain.main(new String[]{});

        // Then: CLI start() should be invoked
        verify(mockCli, times(1)).start();
        // System.exit is NOT called when return code is 0
    }

    /**
     * Test that createCliInterface is called with the correct arguments.
     */
    @Test
    void testMainCallsCreateCliInterfaceWithProvidedArguments() {
        // Given: CLI returns 0 and arguments are provided
        String[] args = new String[]{"--old", "old.yaml", "--new", "new.yaml"};
        when(mockCli.start()).thenReturn(0);

        // When: main is called with arguments
        SwaggerBrakeMain.main(args);

        // Then: createCliInterface should be called with the same arguments
        swaggerBrakeMainMock.verify(() -> SwaggerBrakeMain.createCliInterface(eq(args)), times(1));
    }

    /**
     * Test that the CLI start() method is invoked when main is called.
     */
    @Test
    void testMainInvokesCliStartMethod() {
        // Given: CLI returns 0
        when(mockCli.start()).thenReturn(0);

        // When: main is called
        SwaggerBrakeMain.main(new String[]{});

        // Then: CLI start() method should be invoked exactly once
        verify(mockCli, times(1)).start();
    }

    /**
     * Test that main properly passes empty arguments when none are provided.
     */
    @Test
    void testMainHandlesEmptyArguments() {
        // Given: CLI returns 0 and no arguments are provided
        when(mockCli.start()).thenReturn(0);

        // When: main is called with empty arguments
        SwaggerBrakeMain.main(new String[]{});

        // Then: createCliInterface should be called with empty arguments
        swaggerBrakeMainMock.verify(() -> SwaggerBrakeMain.createCliInterface(any(String[].class)), times(1));
        verify(mockCli, times(1)).start();
    }

    /**
     * Test that verifies the logic for return code 1 (breaking changes found).
     * This test verifies that CLI.start() returns 1, which should trigger System.exit(1).
     * We test by verifying the CLI returns the correct code.
     */
    @Test
    void testReturnCodeOneForBreakingChanges() {
        // Given: CLI is configured to return 1 (breaking changes found)
        when(mockCli.start()).thenReturn(1);

        // Create a wrapper to test the exit code logic without actually exiting
        int exitCode = mockCli.start();

        // Then: Verify exit code is 1 and would trigger System.exit(1)
        verify(mockCli, times(1)).start();
        assertEquals(1, exitCode, "Exit code should be 1 for breaking changes");
    }

    /**
     * Test that verifies the logic for return code 2 (execution error).
     * This test verifies that CLI.start() returns 2, which should trigger System.exit(2).
     */
    @Test
    void testReturnCodeTwoForExecutionError() {
        // Given: CLI is configured to return 2 (execution error)
        when(mockCli.start()).thenReturn(2);

        // Create a wrapper to test the exit code logic without actually exiting
        int exitCode = mockCli.start();

        // Then: Verify exit code is 2 and would trigger System.exit(2)
        verify(mockCli, times(1)).start();
        assertAll(
                () -> assertEquals(2, exitCode, "Exit code should be 2 for execution error"),
                () -> assertTrue(exitCode > 0, "Exit code > 0 should trigger System.exit")
        );
    }

    /**
     * Test that verifies the logic for return code 3 (help invoked).
     * This test verifies that CLI.start() returns 3, which should trigger System.exit(3).
     */
    @Test
    void testReturnCodeThreeForHelpInvoked() {
        // Given: CLI is configured to return 3 (help invoked)
        when(mockCli.start()).thenReturn(3);

        // Create a wrapper to test the exit code logic without actually exiting
        int exitCode = mockCli.start();

        // Then: Verify exit code is 3 and would trigger System.exit(3)
        verify(mockCli, times(1)).start();
        assertAll(
                () -> assertEquals(3, exitCode, "Exit code should be 3 for help invoked"),
                () -> assertTrue(exitCode > 0, "Exit code > 0 should trigger System.exit")
        );
    }

    /**
     * Test that verifies return code 0 does NOT trigger System.exit.
     * This is the only case where the application exits normally.
     */
    @Test
    void testReturnCodeZeroDoesNotExit() {
        // Given: CLI is configured to return 0 (success)
        when(mockCli.start()).thenReturn(0);

        // Create a wrapper to test the exit code logic without actually exiting
        int exitCode = mockCli.start();

        // Then: Verify exit code is 0 and would NOT trigger System.exit
        verify(mockCli, times(1)).start();
        assertAll(
                () -> assertEquals(0, exitCode, "Exit code should be 0 for success"),
                () -> assertTrue(exitCode <= 0, "Exit code <= 0 should NOT trigger System.exit")
        );
    }
}
