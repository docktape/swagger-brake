package com.docktape.swagger.brake.integration.v3.openapi30x;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;

@ExtendWith(SpringExtension.class)
class OpenApi301FullSpecTest extends AbstractSwaggerBrakeIntTest {
    private static final String SWAGGER_3_0_1_FULL = "swaggers/v3/openapi30x/swagger_301_full.json";

    @Test
    void testOpenApi301LoadsWithoutBreakingChanges() {
        Options options = new Options();
        options.setOldApiPath(SWAGGER_3_0_1_FULL);
        options.setNewApiPath(SWAGGER_3_0_1_FULL);
        options.setStrictValidation(false);

        Collection<BreakingChange> result = execute(options);

        assertThat(result).isNullOrEmpty();
    }
}
