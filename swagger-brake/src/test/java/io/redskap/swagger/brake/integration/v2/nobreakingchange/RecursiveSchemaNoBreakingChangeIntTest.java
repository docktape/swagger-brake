package io.redskap.swagger.brake.integration.v2.nobreakingchange;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import io.redskap.swagger.brake.core.BreakingChange;
import io.redskap.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RecursiveSchemaNoBreakingChangeIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testNoBreakingChangeWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/recursive/schema.json";
        String newApiPath = "swaggers/v2/nobreakingchange/recursive/schema_v2.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testNoBreakingChangeWorksCorrectly2() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/recursive2/swagger.json";
        String newApiPath = "swaggers/v2/nobreakingchange/recursive2/swagger.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}

