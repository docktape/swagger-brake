package com.docktape.swagger.brake.integration.v3.request.requestbody.anyof;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestTypeAttributeRemovedBreakingChange;
import com.docktape.swagger.brake.core.rule.request.RequestTypeChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class V3RequestBodyAnyOfIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testV3RequestBodyAnyOfPropertyRemovedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/anyof/propertydeleted/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/anyof/propertydeleted/petstore_v2.yaml";
        RequestTypeAttributeRemovedBreakingChange breakingChange = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PATCH, "id");
        Collection<BreakingChange> expected = Collections.singletonList(breakingChange);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    void testV3RequestBodyAnyOfPropertyTypeChangedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/anyof/propertytypechanged/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/anyof/propertytypechanged/petstore_v2.yaml";
        RequestTypeChangedBreakingChange breakingChange = new RequestTypeChangedBreakingChange("/pet", HttpMethod.PATCH, "id", "integer", "string");
        Collection<BreakingChange> expected = Collections.singletonList(breakingChange);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    void testV3RequestBodyAnyOfSamePropertyNoBreakingChange() {
        // given
        String apiPath = "swaggers/v3/request/requestbody/anyof/nobreakingchange/sameattribute/petstore.yaml";
        // when
        Collection<BreakingChange> result = execute(apiPath, apiPath);
        // then
        assertThat(result).isEmpty();
    }
}