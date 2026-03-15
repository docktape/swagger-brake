package com.docktape.swagger.brake.integration.v3.request.requestbody.nullcontent;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class V3RequestBodyNullContentIntTest extends AbstractSwaggerBrakeIntTest {

    private static final String SPEC = "swaggers/v3/request/requestbody/nullcontent/swagger_3.1.json";

    @Test
    void testRequestBodyWithNullContentDoesNotThrowNpe() {
        // given - a spec where requestBody has no 'content' field (content is null)
        // when
        Collection<BreakingChange> result = execute(SPEC, SPEC);
        // then
        assertThat(result).isEmpty();
    }
}
