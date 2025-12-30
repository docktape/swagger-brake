package com.docktape.swagger.brake.integration.v3.request.requestbody.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class MemoryIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testHugeFineractApiMissingSchemaType() {
        // given
        String oldApiPath = "swaggers/v3/memory/fineract/swagger_3.1.json";
        String newApiPath = "swaggers/v3/memory/fineract/swagger_3.2.json";
        // when
        Collection<BreakingChange> result = execute(createOptions(oldApiPath, newApiPath));
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testSwagger31MinimalReproducerValidation() {
        // given
        String oldApiPath = "swaggers/v3/openapi31x/minimal_reproducer_fixed.json";
        String newApiPath = "swaggers/v3/openapi31x/minimal_reproducer_fixed.json";
        // when
        Collection<BreakingChange> result = execute(createOptions(oldApiPath, newApiPath));
        // then
        assertThat(result).isEmpty();
    }

    private Options createOptions(String oldApiPath, String newApiPath) {
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);
        return options;
    }
}