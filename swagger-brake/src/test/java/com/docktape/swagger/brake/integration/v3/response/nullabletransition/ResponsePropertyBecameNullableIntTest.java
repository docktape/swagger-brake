package com.docktape.swagger.brake.integration.v3.response.nullabletransition;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.response.ResponsePropertyBecameNullableBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponsePropertyBecameNullableIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testResponsePropertyBecameNullableIsBreakingChange() {
        // given - old spec has no nullable, new spec adds nullable: true
        String oldApiPath = "swaggers/v3/response/nullabletransition/petstore.yaml";
        String newApiPath = "swaggers/v3/response/nullabletransition/petstore_v2.yaml";
        ResponsePropertyBecameNullableBreakingChange expected =
            new ResponsePropertyBecameNullableBreakingChange("/pet", HttpMethod.GET, "200", "name");
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).contains(expected);
    }

    @Test
    void testResponsePropertyBecameNonNullableIsNotBreakingChange() {
        // given - old spec has nullable: true, new spec removes nullable (reverse direction is not breaking for responses)
        String oldApiPath = "swaggers/v3/response/nullabletransition/petstore_v2.yaml";
        String newApiPath = "swaggers/v3/response/nullabletransition/petstore.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
