package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestTypeXExtensibleEnumValueDeletedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestTypeXExtensibleEnumValueDeletedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testXExtensibleEnumValueRemovedIsDetectedAsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v2/request/xextensibleenumvaluedeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/xextensibleenumvaluedeleted/petstore_v2.yaml";
        RequestTypeXExtensibleEnumValueDeletedBreakingChange expected =
            new RequestTypeXExtensibleEnumValueDeletedBreakingChange("/order", HttpMethod.POST, "status", "approved");
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(expected)
        );
    }

    @Test
    void testXExtensibleEnumValueAddedIsNotBreakingChange() {
        // given
        String oldApiPath = "swaggers/v2/request/xextensibleenumvaluedeleted/nobreakingchange/petstore.yaml";
        String newApiPath = "swaggers/v2/request/xextensibleenumvaluedeleted/nobreakingchange/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
