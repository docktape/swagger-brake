package com.docktape.swagger.brake.integration.v3.openapi31;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestPropertyConstChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestPropertyConstChangedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testRequestConstAddedIsBreaking() {
        String oldApiPath = "swaggers/v3/openapi31x/const-request/petstore_v1.json";
        String newApiPath = "swaggers/v3/openapi31x/const-request/petstore_v2.json";
        RequestPropertyConstChangedBreakingChange constAdded =
            new RequestPropertyConstChangedBreakingChange("/orders", HttpMethod.POST, "status", null, "pending");
        RequestPropertyConstChangedBreakingChange constChanged =
            new RequestPropertyConstChangedBreakingChange("/orders", HttpMethod.POST, "source", "web", "mobile");

        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).contains(constAdded),
            () -> assertThat(result).contains(constChanged)
        );
    }
}
