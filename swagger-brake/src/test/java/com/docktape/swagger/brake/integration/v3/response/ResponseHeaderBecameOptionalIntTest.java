package com.docktape.swagger.brake.integration.v3.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.response.ResponseHeaderBecameOptionalBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponseHeaderBecameOptionalIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testRequiredResponseHeaderBecomingOptionalIsBreaking() {
        // given
        String oldApiPath = "swaggers/v3/response/headerbecameoptional/petstore.yaml";
        String newApiPath = "swaggers/v3/response/headerbecameoptional/petstore_v2.yaml";
        ResponseHeaderBecameOptionalBreakingChange bc =
            new ResponseHeaderBecameOptionalBreakingChange("/pet", HttpMethod.GET, "200", "X-Rate-Limit");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).hasSameElementsAs(expected)
        );
    }
}
