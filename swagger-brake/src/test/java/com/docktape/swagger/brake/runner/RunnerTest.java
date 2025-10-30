package com.docktape.swagger.brake.runner;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.docktape.swagger.brake.core.BreakChecker;
import com.docktape.swagger.brake.report.ReporterFactory;
import com.docktape.swagger.brake.runner.download.ArtifactDownloaderHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RunnerTest {
    @Mock
    private BreakChecker breakChecker;

    @Mock
    private ReporterFactory reporterFactory;

    @Mock
    private ArtifactDownloaderHandler artifactDownloaderHandler;

    @Mock
    private OptionsValidator optionsValidator;

    @InjectMocks
    private Runner underTest;

    @Test
    public void testRunShouldThrowExceptionWhenOldApiPathIsNotPresent() {
        // given
        Options options = new Options();
        options.setNewApiPath("newApi");
        // when
        assertThatThrownBy(() -> underTest.run(options)).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }

    @Test
    public void testRunShouldThrowExceptionWhenNewApiPathIsNotPresent() {
        // given
        Options options = new Options();
        options.setOldApiPath("oldApi");
        // when
        assertThatThrownBy(() -> underTest.run(options)).isExactlyInstanceOf(IllegalArgumentException.class);
        // then exception thrown
    }
}