package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestTypeEnumValueDeletedBreakingChange;
import com.docktape.swagger.brake.core.rule.response.ResponseTypeEnumValueDeletedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponseTypeEnumValueDeletedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testResponseTypeEnumValueDeletedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/response/typeenumvaluedeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/response/typeenumvaluedeleted/petstore_v2.yaml";
        RequestTypeEnumValueDeletedBreakingChange bc1 = new RequestTypeEnumValueDeletedBreakingChange("/store/order", HttpMethod.POST, "status.approved");
        ResponseTypeEnumValueDeletedBreakingChange bc2 = new ResponseTypeEnumValueDeletedBreakingChange("/store/order", HttpMethod.POST, "status.approved");
        ResponseTypeEnumValueDeletedBreakingChange bc3 = new ResponseTypeEnumValueDeletedBreakingChange("/store/order/{orderId}", HttpMethod.GET, "status.approved");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2, bc3);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }
}
