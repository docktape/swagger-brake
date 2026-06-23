package com.docktape.swagger.brake.integration.v3.openapi31;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.response.ResponsePropertyConstChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponsePropertyConstChangedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testResponseConstChangedIsBreaking() {
        String oldApiPath = "swaggers/v3/openapi31x/const-response/petstore_v1.json";
        String newApiPath = "swaggers/v3/openapi31x/const-response/petstore_v2.json";
        ResponsePropertyConstChangedBreakingChange expected =
            new ResponsePropertyConstChangedBreakingChange("/orders/{id}", HttpMethod.GET, "200", "currency");

        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(expected)
        );
    }

    @Test
    void testResponseConstUnchangedIsNotBreaking() {
        String oldApiPath = "swaggers/v3/openapi31x/const-unchanged/petstore_v1.json";
        String newApiPath = "swaggers/v3/openapi31x/const-unchanged/petstore_v2.json";

        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);

        assertThat(result).isEmpty();
    }
}
