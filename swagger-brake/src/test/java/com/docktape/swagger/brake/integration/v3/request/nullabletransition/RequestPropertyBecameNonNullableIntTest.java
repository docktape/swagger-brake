package com.docktape.swagger.brake.integration.v3.request.nullabletransition;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestPropertyBecameNonNullableBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestPropertyBecameNonNullableIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testRequestPropertyBecameNonNullableIsBreakingChange() {
        // given - old spec has nullable: true, new spec removes nullable
        String oldApiPath = "swaggers/v3/request/nullabletransition/petstore.yaml";
        String newApiPath = "swaggers/v3/request/nullabletransition/petstore_v2.yaml";
        RequestPropertyBecameNonNullableBreakingChange expected = new RequestPropertyBecameNonNullableBreakingChange("/pet", HttpMethod.POST, "name");
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).contains(expected);
    }

    @Test
    void testRequestPropertyBecameNullableIsNotBreakingChange() {
        // given - old spec has no nullable, new spec adds nullable: true (reverse direction is not breaking for requests)
        String oldApiPath = "swaggers/v3/request/nullabletransition/petstore_v2.yaml";
        String newApiPath = "swaggers/v3/request/nullabletransition/petstore.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
