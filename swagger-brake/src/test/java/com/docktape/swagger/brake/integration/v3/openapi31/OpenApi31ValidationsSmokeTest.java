package com.docktape.swagger.brake.integration.v3.openapi31;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpenApi31ValidationsSmokeTest extends AbstractSwaggerBrakeIntTest {
    private static final String SWAGGER_3_1_VALIDATIONS = "swaggers/v3/openapi31x/swagger-3.1-validations.json";

    @Test
    void testOpenApi31ValidationsSelfStrictComparisonHasNoBreakingChanges() {
        String apiPath = SWAGGER_3_1_VALIDATIONS;
        Options options = new Options();
        options.setOldApiPath(apiPath);
        options.setNewApiPath(apiPath);
        options.setStrictValidation(true);

        Throwable result = Assertions.catchThrowable(() -> execute(options));

        assertAll(
                () -> assertThat(result).isInstanceOf(IllegalStateException.class),
                () -> assertThat(result.getMessage()).contains("Schema has null type")
        );
    }

    @Test
    void testOpenApi31ValidationsSelfNonStrictComparisonHasNoBreakingChanges() {
        String apiPath = SWAGGER_3_1_VALIDATIONS;
        Options options = new Options();
        options.setOldApiPath(apiPath);
        options.setNewApiPath(apiPath);
        options.setStrictValidation(false);

        Collection<BreakingChange> result = execute(options);

        assertThat(result)
            .as("Self-comparison of complex 3.1 validations spec should yield no breaking changes")
            .isNotNull()
            .isEmpty();
    }
}
