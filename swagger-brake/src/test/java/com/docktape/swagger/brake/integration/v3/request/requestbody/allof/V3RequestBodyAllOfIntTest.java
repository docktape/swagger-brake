package com.docktape.swagger.brake.integration.v3.request.requestbody.allof;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestTypeAttributeRemovedBreakingChange;
import com.docktape.swagger.brake.core.rule.request.RequestTypeChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class V3RequestBodyAllOfIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testV3RequestBodyAllOfPropertyRemovedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/allof/propertydeleted/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/allof/propertydeleted/petstore_v2.yaml";
        RequestTypeAttributeRemovedBreakingChange breakingChange1 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PATCH, "bark");
        Collection<BreakingChange> expected = Arrays.asList(breakingChange1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testV3RequestBodyAllOfPropertyTypeChangedIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/allof/propertytypechanged/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/allof/propertytypechanged/petstore_v2.yaml";
        RequestTypeChangedBreakingChange breakingChange1 = new RequestTypeChangedBreakingChange("/pet", HttpMethod.PATCH, "bark", "integer", "string");
        RequestTypeChangedBreakingChange breakingChange2 = new RequestTypeChangedBreakingChange("/pet", HttpMethod.PATCH, "id", "boolean", "number");
        Collection<BreakingChange> expected = Arrays.asList(breakingChange1, breakingChange2);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(2);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testV3RequestBodyAllOfSamePropertyNoBreakingChange() {
        // given
        String apiPath = "swaggers/v3/request/requestbody/allof/nobreakingchange/sameattribute/petstore.yaml";
        // when
        Collection<BreakingChange> result = execute(apiPath, apiPath);
        // then
        assertThat(result).isEmpty();
    }
}