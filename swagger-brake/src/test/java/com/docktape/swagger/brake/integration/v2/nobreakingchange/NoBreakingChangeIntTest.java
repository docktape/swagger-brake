package com.docktape.swagger.brake.integration.v2.nobreakingchange;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class NoBreakingChangeIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testNoBreakingChangeWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/petstore.yaml";
        String newApiPath = "swaggers/v2/nobreakingchange/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testNoBreakingChangeWhenSameApiUsedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/petstore.yaml";
        String newApiPath = "swaggers/v2/nobreakingchange/petstore.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
