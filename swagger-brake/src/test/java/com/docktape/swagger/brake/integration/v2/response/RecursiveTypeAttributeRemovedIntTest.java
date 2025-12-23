package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.response.ResponseTypeAttributeRemovedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RecursiveTypeAttributeRemovedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testResponseTypeChangeIsBreakingChangeWhenExistingAttributeRemoved() {
        // given
        String oldApiPath = "swaggers/v2/response/recursiveresponseattributeremoved/schema.json";
        String newApiPath = "swaggers/v2/response/recursiveresponseattributeremoved/schema_v2.json";
        ResponseTypeAttributeRemovedBreakingChange bc1 =
            new ResponseTypeAttributeRemovedBreakingChange("/api/v1/audits/summary/{businessId}", HttpMethod.GET, "200", "unverifiedPayoffBreakdown.amountApplied");
        ResponseTypeAttributeRemovedBreakingChange bc2 =
            new ResponseTypeAttributeRemovedBreakingChange("/api/v1/audits/summary/{businessId}", HttpMethod.GET, "200", "unverifiedPayoffBreakdown.children.amountApplied");
        Collection<BreakingChange> expected = List.of(bc1, bc2);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }
}
