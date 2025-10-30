package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestParameterDeletedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RequestParameterDeletedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testRequestParameterDeletedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdeleted/petstore_v2.yaml";
        RequestParameterDeletedBreakingChange bc = new RequestParameterDeletedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }
}
