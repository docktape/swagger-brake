package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.response.ResponseConstraintChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponseConstraintChangedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testResponseConstraintLooseningIsBreakingChangeWhenMaxLengthIncreases() {
        // given
        String oldApiPath = "swaggers/v2/response/constraintloosened/maxlength/swagger-old.json";
        String newApiPath = "swaggers/v2/response/constraintloosened/maxlength/swagger-new.json";
        ResponseConstraintChangedBreakingChange expected = new ResponseConstraintChangedBreakingChange(
            "/store/order/{orderId}", HttpMethod.GET, "200", "status", new ConstraintChange("maxLength", 20, 10));
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).hasSameElementsAs(Collections.singletonList(expected))
        );
    }

    @Test
    void testResponseConstraintTighteningIsNotBreakingChangeWhenMaxLengthDecreases() {
        // given
        String oldApiPath = "swaggers/v2/response/constrainttightened/maxlength/swagger-old.json";
        String newApiPath = "swaggers/v2/response/constrainttightened/maxlength/swagger-new.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
