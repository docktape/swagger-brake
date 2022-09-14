package io.redskap.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;

import io.redskap.swagger.brake.core.BreakingChange;
import io.redskap.swagger.brake.core.model.HttpMethod;
import io.redskap.swagger.brake.core.rule.request.RequestParameterEnumValueDeletedBreakingChange;
import io.redskap.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RequestParameterEnumValueDeletedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testRequestParameterEnumValueDeletedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterenumvaluedeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterenumvaluedeleted/petstore_v2.yaml";
        RequestParameterEnumValueDeletedBreakingChange bc = new RequestParameterEnumValueDeletedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status", "pending");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }
}
