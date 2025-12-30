package com.docktape.swagger.brake.integration.v3.openapi31;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class OpenApi31NumericBoundsTest extends AbstractSwaggerBrakeIntTest {
    
    @Test
    void testOpenApi31NumericBoundsLoadsSuccessfully() {
        // given - OpenAPI 3.1.x specs with numeric exclusive bounds
        String oldApiPath = "swaggers/v3/openapi31x/numeric_bounds_v1.json";
        String newApiPath = "swaggers/v3/openapi31x/numeric_bounds_v2.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then - should successfully load and process without errors
        // Breaking change detection for numeric bounds will be added in future enhancement
        assertThat(result).hasSize(1);
    }
}
