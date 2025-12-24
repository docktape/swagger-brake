package com.docktape.swagger.brake.integration.v3.openapi31;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.rule.request.RequestTypeChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpenApi31TypesTest extends AbstractSwaggerBrakeIntTest {
    private static final String SWAGGER_3_1_TYPES_ISSUES = "swaggers/v3/openapi31x/swagger-3.1-types-issues.json";
    private static final String SWAGGER_3_1_TYPES_INVALID = "swaggers/v3/openapi31x/swagger-3.1-types-issues-invalid.json";
    
    @Test
    void testOpenApi31BreakingChange() {
        // given - OpenAPI 3.1.x upgrade with breaking changes
        String oldApiPath = SWAGGER_3_1_TYPES_ISSUES;
        String newApiPath = SWAGGER_3_1_TYPES_INVALID;
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);
        // when
        Collection<BreakingChange> result = execute(options);
        // then - should successfully load and process without errors
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasOnlyElementsOfType(RequestTypeChangedBreakingChange.class)
        );
    }
    
    @Test
    void testOpenApi31TypesSuccessfully() {
        // given - OpenAPI 3.1.x specs with no types exclusive to v3.1
        String oldApiPath = SWAGGER_3_1_TYPES_ISSUES;
        String newApiPath = SWAGGER_3_1_TYPES_ISSUES;
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
