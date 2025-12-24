package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestTypeAttributeRemovedBreakingChange;
import com.docktape.swagger.brake.core.rule.response.ResponseTypeAttributeRemovedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ResponseTypeAttributeRemovedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testResponseTypeChangeIsBreakingChangeWhenExistingAttributeRemoved() {
        // given
        String oldApiPath = "swaggers/v2/response/attributeremoved/petstore.yaml";
        String newApiPath = "swaggers/v2/response/attributeremoved/petstore_v2.yaml";
        ResponseTypeAttributeRemovedBreakingChange bc1 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category");
        ResponseTypeAttributeRemovedBreakingChange bc2 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category.id");
        ResponseTypeAttributeRemovedBreakingChange bc3 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category.name");
        ResponseTypeAttributeRemovedBreakingChange bc4 = new ResponseTypeAttributeRemovedBreakingChange("/pet/{petId}", HttpMethod.GET, "200", "category");
        ResponseTypeAttributeRemovedBreakingChange bc5 = new ResponseTypeAttributeRemovedBreakingChange("/pet/{petId}", HttpMethod.GET, "200", "category.id");
        ResponseTypeAttributeRemovedBreakingChange bc6 = new ResponseTypeAttributeRemovedBreakingChange("/pet/{petId}", HttpMethod.GET, "200", "category.name");
        ResponseTypeAttributeRemovedBreakingChange bc7 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByTags", HttpMethod.GET, "200", "category");
        ResponseTypeAttributeRemovedBreakingChange bc8 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByTags", HttpMethod.GET, "200", "category.id");
        ResponseTypeAttributeRemovedBreakingChange bc9 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByTags", HttpMethod.GET, "200", "category.name");
        RequestTypeAttributeRemovedBreakingChange bc10 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PUT, "category");
        RequestTypeAttributeRemovedBreakingChange bc11 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PUT, "category.id");
        RequestTypeAttributeRemovedBreakingChange bc12 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PUT, "category.name");
        RequestTypeAttributeRemovedBreakingChange bc13 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.POST, "category");
        RequestTypeAttributeRemovedBreakingChange bc14 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.POST, "category.id");
        RequestTypeAttributeRemovedBreakingChange bc15 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.POST, "category.name");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2, bc3, bc4, bc5, bc6, bc7, bc8, bc9, bc10, bc11, bc12, bc13, bc14, bc15);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(15),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testResponseTypeChangeIsBreakingChangeWhenExistingDeepAttributeRemoved() {
        // given
        String oldApiPath = "swaggers/v2/response/deepattributeremoved/petstore.yaml";
        String newApiPath = "swaggers/v2/response/deepattributeremoved/petstore_v2.yaml";
        ResponseTypeAttributeRemovedBreakingChange bc1 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category.name");
        ResponseTypeAttributeRemovedBreakingChange bc2 = new ResponseTypeAttributeRemovedBreakingChange("/pet/{petId}", HttpMethod.GET, "200", "category.name");
        ResponseTypeAttributeRemovedBreakingChange bc3 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByTags", HttpMethod.GET, "200", "category.name");
        RequestTypeAttributeRemovedBreakingChange bc4 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PUT, "category.name");
        RequestTypeAttributeRemovedBreakingChange bc5 = new RequestTypeAttributeRemovedBreakingChange("/pet", HttpMethod.POST, "category.name");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2, bc3, bc4, bc5);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(5),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testResponseTypeChangeIsNotBreakingChangeWhenDifferentTypeIsUsedButSameAttributes() {
        // given
        String oldApiPath = "swaggers/v2/response/differenttypesameattributes/petstore.yaml";
        String newApiPath = "swaggers/v2/response/differenttypesameattributes/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testResponseTypeChangeIsBreakingChangeWhenDifferentTypeIsUsedWithDifferentAttributes() {
        // given
        String oldApiPath = "swaggers/v2/response/differenttypesdifferentattributes/petstore.yaml";
        String newApiPath = "swaggers/v2/response/differenttypesdifferentattributes/petstore_v2.yaml";
        ResponseTypeAttributeRemovedBreakingChange bc1 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category");
        ResponseTypeAttributeRemovedBreakingChange bc2 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category.id");
        ResponseTypeAttributeRemovedBreakingChange bc3 = new ResponseTypeAttributeRemovedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "category.name");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2, bc3);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testResponseTypeChangeIsNotBreakingChangeWhenDeprecatedExistingAttributeRemoved() {
        // given
        String oldApiPath = "swaggers/v3/response/attributeremoved-deprecated/petstore.yaml";
        String newApiPath = "swaggers/v3/response/attributeremoved-deprecated/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testResponseTypeChangeIsBreakingChangeWhenExistingAttributeRemoved_V3Schema() {
        // given
        String oldApiPath = "swaggers/v3/response/attributeremoved/petstore.yaml";
        String newApiPath = "swaggers/v3/response/attributeremoved/petstore_v2.yaml";
        ResponseTypeAttributeRemovedBreakingChange bc1 = new ResponseTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PATCH, "201", "id");
        ResponseTypeAttributeRemovedBreakingChange bc2 = new ResponseTypeAttributeRemovedBreakingChange("/pet", HttpMethod.PATCH, "201", "breed");
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