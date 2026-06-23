package com.docktape.swagger.brake.integration.v3.request.cookieparameterdeleted;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
class CookieParameterDeletedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testRequiredCookieParameterDeletedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/cookieparameterdeleted/petstore.yaml";
        String newApiPath = "swaggers/v3/request/cookieparameterdeleted/petstore_v2.yaml";
        RequestParameterDeletedBreakingChange bc = new RequestParameterDeletedBreakingChange("/pets", HttpMethod.GET, "session_id");
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
    void testCookieParameterUnchangedIsNotBreakingChange() {
        // given
        String apiPath = "swaggers/v3/request/cookieparameterdeleted/petstore.yaml";
        // when
        Collection<BreakingChange> result = execute(apiPath, apiPath);
        // then
        assertThat(result).isEmpty();
    }
}
