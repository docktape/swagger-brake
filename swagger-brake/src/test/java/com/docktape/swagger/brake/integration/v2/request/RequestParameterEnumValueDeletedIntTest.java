package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestParameterEnumValueDeletedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestParameterEnumValueDeletedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testRequestParameterEnumValueDeletedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterenumvaluedeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterenumvaluedeleted/petstore_v2.yaml";
        RequestParameterEnumValueDeletedBreakingChange bc = new RequestParameterEnumValueDeletedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status", "pending");
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
