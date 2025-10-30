package com.docktape.swagger.brake.cli.options;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;

import com.docktape.swagger.brake.cli.options.handler.CliOptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
public class CliOptionsProviderTest {
    @Mock
    private List<CliOptionHandler> handlers;
    @Mock
    private CliHelpProvider helpProvider;
    @Mock
    private Environment environment;

    @InjectMocks
    private CliOptionsProvider underTest;

    @Test
    public void testProvideShouldThrowExceptionWhenHelpIsRequired() {
        // given
        given(environment.getProperty("help")).willReturn("");
        // when
        Throwable exception = catchThrowable(() -> underTest.provide());
        // then
        assertThat(exception).isInstanceOf(CliHelpException.class);
        then(helpProvider).should().getHelp();
    }
}