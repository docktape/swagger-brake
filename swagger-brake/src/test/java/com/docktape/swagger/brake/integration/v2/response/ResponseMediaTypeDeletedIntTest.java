package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.rule.response.ResponseMediaTypeDeletedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponseMediaTypeDeletedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testResponseMediaTypeDeletedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/response/mediatypedeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/response/mediatypedeleted/petstore_v2.yaml";
        ResponseMediaTypeDeletedBreakingChange bc = new ResponseMediaTypeDeletedBreakingChange("/pet/findByStatus", HttpMethod.GET, new MediaType("application/json"));
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
