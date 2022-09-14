package io.redskap.swagger.brake.integration.v3.request.requestbody.oneof;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;

import io.redskap.swagger.brake.core.BreakingChange;
import io.redskap.swagger.brake.core.model.HttpMethod;
import io.redskap.swagger.brake.core.rule.request.RequestTypeAttributeRemovedBreakingChange;
import io.redskap.swagger.brake.core.rule.request.RequestTypeChangedBreakingChange;
import io.redskap.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class V3RequestBodyOneOfIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testV3RequestBodyOneOfPropertyRemovedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/oneof/propertydeleted/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/oneof/propertydeleted/petstore_v2.yaml";
        RequestTypeAttributeRemovedBreakingChange breakingChange = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PATCH, "id");
        Collection<BreakingChange> expected = Collections.singletonList(breakingChange);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testV3RequestBodyOneOfPropertyTypeChangedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/oneof/propertytypechanged/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/oneof/propertytypechanged/petstore_v2.yaml";
        RequestTypeChangedBreakingChange breakingChange = new RequestTypeChangedBreakingChange("/pet", HttpMethod.PATCH, "id", "integer", "string");
        Collection<BreakingChange> expected = Collections.singletonList(breakingChange);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }
}