package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.List;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.rule.response.ResponseMediaTypeDeletedBreakingChange;
import com.docktape.swagger.brake.core.rule.response.ResponseMediaTypeGeneralizedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponseMediaTypeGeneralizedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testResponseMediaTypeGeneralizedIsDetected() {
        // given
        String oldApiPath = "swaggers/v2/response/mediatypegeneralized/petstore.yaml";
        String newApiPath = "swaggers/v2/response/mediatypegeneralized/petstore_v2.yaml";
        ResponseMediaTypeGeneralizedBreakingChange generalizedBc = new ResponseMediaTypeGeneralizedBreakingChange(
            "/pet/findByStatus", HttpMethod.GET, "200", "application/vnd.api+json", "application/json");
        ResponseMediaTypeDeletedBreakingChange deletedBc = new ResponseMediaTypeDeletedBreakingChange(
            "/pet/findByStatus", HttpMethod.GET, new MediaType("application/vnd.api+json"));
        Collection<BreakingChange> expected = List.of(generalizedBc, deletedBc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).hasSameElementsAs(expected)
        );
    }
}
