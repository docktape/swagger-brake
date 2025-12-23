package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestTypeChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestTypeChangedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testRequestTypeChangeIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v2/request/attributetypechanged/petstore.yaml";
        String newApiPath = "swaggers/v2/request/attributetypechanged/petstore_v2.yaml";
        RequestTypeChangedBreakingChange bc1 = new RequestTypeChangedBreakingChange("/pet", HttpMethod.PUT, "id", "integer", "string");
        RequestTypeChangedBreakingChange bc2 = new RequestTypeChangedBreakingChange("/pet", HttpMethod.POST, "id", "integer", "string");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }
}