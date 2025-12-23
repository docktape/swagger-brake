package com.docktape.swagger.brake.integration.v3.openapi31;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpenApi31VersionCompatibilityTest extends AbstractSwaggerBrakeIntTest {
    private static final String OPENAPI_3_0_SPEC = "swaggers/v3/request/attributeremoved/petstore.yaml";
    private static final String OPENAPI_3_1_SPEC = "swaggers/v3/openapi31x/swagger-3.1-types-issues.json";

    @Test
    void testMixedVersionComparisonExecutesWithoutFailure() {
        Options options = new Options();
        options.setOldApiPath(OPENAPI_3_0_SPEC);
        options.setNewApiPath(OPENAPI_3_1_SPEC);
        options.setStrictValidation(false);
        
        Collection<BreakingChange> result = execute(options);

        assertThat(result)
            .as("Mixed OpenAPI 3.0 vs 3.1 comparison should currently execute without throwing")
            .isNotNull();
    }
}
