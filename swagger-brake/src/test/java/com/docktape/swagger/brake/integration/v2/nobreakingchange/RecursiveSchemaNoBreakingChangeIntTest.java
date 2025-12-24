package com.docktape.swagger.brake.integration.v2.nobreakingchange;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RecursiveSchemaNoBreakingChangeIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testNoBreakingChangeWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/recursive/schema.json";
        String newApiPath = "swaggers/v2/nobreakingchange/recursive/schema_v2.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testNoBreakingChangeWorksCorrectly2() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/recursive2/swagger.json";
        String newApiPath = "swaggers/v2/nobreakingchange/recursive2/swagger.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}

