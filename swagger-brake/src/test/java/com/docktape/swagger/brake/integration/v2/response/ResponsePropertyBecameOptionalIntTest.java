package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.response.ResponsePropertyBecameOptionalBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponsePropertyBecameOptionalIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testRequiredPropertyBecomingOptionalIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/response/property-became-optional/petstore.yaml";
        String newApiPath = "swaggers/v3/response/property-became-optional/petstore_v2.yaml";
        ResponsePropertyBecameOptionalBreakingChange expected = new ResponsePropertyBecameOptionalBreakingChange(
                "/pet", HttpMethod.GET, "200", "name");
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).contains(expected)
        );
    }

    @Test
    void testPropertyBecomingRequiredIsNotBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/response/property-became-required/petstore.yaml";
        String newApiPath = "swaggers/v3/response/property-became-required/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).doesNotHaveAnyElementsOfTypes(ResponsePropertyBecameOptionalBreakingChange.class);
    }
}
