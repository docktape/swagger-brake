package com.docktape.swagger.brake.integration.v3.openapi30;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;

@ExtendWith(SpringExtension.class)
class OpenApi30xTest extends AbstractSwaggerBrakeIntTest {
    private static final String SWAGGER_3_0_TYPES_ISSUES = "swaggers/v3/openapi30x/swagger_301_full.json";
    private static final String SWAGGER_3_0_TYPES_INVALID = "swaggers/v3/openapi30x/swagger_301_full_invalid.json";
    
    @Test
    void testOpenApi30BreakingChange() {
        // given - OpenAPI 3.0.x upgrade with breaking changes
        String oldApiPath = SWAGGER_3_0_TYPES_ISSUES;
        String newApiPath = SWAGGER_3_0_TYPES_INVALID;
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);
        // when
        Collection<BreakingChange> result = execute(options);
        // then - should successfully load and process, detecting breaking changes
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(2)
        );
    }
    
    @Test
    void testOpenApi30TypesSuccessfully() {
        // given - OpenAPI 3.0.x specs with no types exclusive to v3.0
        String oldApiPath = SWAGGER_3_0_TYPES_ISSUES;
        String newApiPath = SWAGGER_3_0_TYPES_ISSUES;
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);
        // when
        Collection<BreakingChange> result = execute(options);
        // then - should successfully load and process without errors
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).isEmpty()
        );
    }
}
