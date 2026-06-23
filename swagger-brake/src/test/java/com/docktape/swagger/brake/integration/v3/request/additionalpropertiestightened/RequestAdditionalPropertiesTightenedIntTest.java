package com.docktape.swagger.brake.integration.v3.request.additionalpropertiestightened;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.List;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestAdditionalPropertiesTightenedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestAdditionalPropertiesTightenedIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testAdditionalPropertiesTrueToFalseIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/additionalpropertiestightened/petstore_true_to_false.yaml";
        String newApiPath = "swaggers/v3/request/additionalpropertiestightened/petstore_true_to_false_v2.yaml";
        RequestAdditionalPropertiesTightenedBreakingChange expected =
                new RequestAdditionalPropertiesTightenedBreakingChange("/pet", HttpMethod.POST, "");
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(List.of(expected))
        );
    }

    @Test
    void testAdditionalPropertiesAbsentToFalseIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/additionalpropertiestightened/petstore_absent_to_false.yaml";
        String newApiPath = "swaggers/v3/request/additionalpropertiestightened/petstore_absent_to_false_v2.yaml";
        RequestAdditionalPropertiesTightenedBreakingChange expected =
                new RequestAdditionalPropertiesTightenedBreakingChange("/pet", HttpMethod.POST, "");
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(List.of(expected))
        );
    }

    @Test
    void testAdditionalPropertiesFalseToFalseIsNotBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/additionalpropertiestightened/petstore_false_to_false.yaml";
        String newApiPath = "swaggers/v3/request/additionalpropertiestightened/petstore_false_to_false_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
