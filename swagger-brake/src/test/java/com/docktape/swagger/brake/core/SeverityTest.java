package com.docktape.swagger.brake.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.core.rule.path.PathDeletedBreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import org.junit.jupiter.api.Test;

class SeverityTest {

    @Test
    void testGetSeverityShouldReturnErrorByDefaultOnExistingBreakingChangeImpl() {
        // given
        BreakingChange breakingChange = new PathDeletedBreakingChange("/api/test", HttpMethod.GET);
        // when
        Severity severity = breakingChange.getSeverity();
        // then
        assertThat(severity).isEqualTo(Severity.ERROR);
    }

    @Test
    void testGetSeverityShouldReturnErrorOnAnonymousImpl() {
        // given
        BreakingChange breakingChange = new BreakingChange() {
            @Override
            public String getMessage() {
                return "test message";
            }

            @Override
            public String getRuleCode() {
                return "R999";
            }
        };
        // when
        Severity severity = breakingChange.getSeverity();
        // then
        assertThat(severity).isEqualTo(Severity.ERROR);
    }
}
