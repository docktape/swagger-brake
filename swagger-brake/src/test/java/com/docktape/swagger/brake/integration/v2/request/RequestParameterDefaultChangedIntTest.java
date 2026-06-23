package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestParameterDefaultChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestParameterDefaultChangedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testDefaultAddedIsDetected() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdefaultchanged/defaultadded/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdefaultchanged/defaultadded/petstore_v2.yaml";
        RequestParameterDefaultChangedBreakingChange bc =
            new RequestParameterDefaultChangedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status", null, "available");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testDefaultChangedIsDetected() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdefaultchanged/defaultchanged/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdefaultchanged/defaultchanged/petstore_v2.yaml";
        RequestParameterDefaultChangedBreakingChange bc =
            new RequestParameterDefaultChangedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status", "available", "pending");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testDefaultRemovedIsDetected() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdefaultchanged/defaultremoved/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdefaultchanged/defaultremoved/petstore_v2.yaml";
        RequestParameterDefaultChangedBreakingChange bc =
            new RequestParameterDefaultChangedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status", "available", null);
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testNoDefaultOnEitherSideIsNotDetected() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdefaultchanged/nodefault/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdefaultchanged/nodefault/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
