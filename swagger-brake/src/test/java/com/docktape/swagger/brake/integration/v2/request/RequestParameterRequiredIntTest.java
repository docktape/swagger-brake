package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestParameterRequiredBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RequestParameterRequiredIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testRequestParameterRequiredWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterrequired/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterrequired/petstore_v2.yaml";
        RequestParameterRequiredBreakingChange bc1 = new RequestParameterRequiredBreakingChange("/pet", HttpMethod.POST, "test");
        RequestParameterRequiredBreakingChange bc2 = new RequestParameterRequiredBreakingChange("/pet/findByStatus", HttpMethod.GET, "test");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(2);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testRequestParameterRequiredWorksForPostObjects() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterrequired/post-object/swagger-old.json";
        String newApiPath = "swaggers/v2/request/parameterrequired/post-object/swagger-new.json";
        RequestParameterRequiredBreakingChange bc1 = new RequestParameterRequiredBreakingChange("/pet", HttpMethod.POST, "name");
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testRequestParameterRequiredWorksForDottedPostObjects() {
        // given
        String oldApiPath = "swaggers/v2/request/dottedparameterrequired/swagger-old.json";
        String newApiPath = "swaggers/v2/request/dottedparameterrequired/swagger-new.json";

        RequestParameterRequiredBreakingChange bc1 = new RequestParameterRequiredBreakingChange("/pet", HttpMethod.POST, "urn:scim:schemas:extension:workspace:1.0:name");
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }
}
