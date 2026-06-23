package com.docktape.swagger.brake.integration.v3.request.requestbody.becamerequired;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestBodyBecameRequiredBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class V3RequestBodyBecameRequiredIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testOptionalRequestBodyBecomingRequiredIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/becamerequired/optionaltorequired/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/becamerequired/optionaltorequired/petstore_v2.yaml";
        RequestBodyBecameRequiredBreakingChange expected = new RequestBodyBecameRequiredBreakingChange("/pet", HttpMethod.POST);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).contains(expected);
    }

    @Test
    void testNoRequestBodyBecomingRequiredIsBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/becamerequired/nobodytorequired/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/becamerequired/nobodytorequired/petstore_v2.yaml";
        RequestBodyBecameRequiredBreakingChange expected = new RequestBodyBecameRequiredBreakingChange("/pet", HttpMethod.POST);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).contains(expected);
    }

    @Test
    void testOptionalRequestBodyRemainingOptionalIsNotBreakingChange() {
        // given
        String oldApiPath = "swaggers/v3/request/requestbody/becamerequired/nobreakingchange/petstore.yaml";
        String newApiPath = "swaggers/v3/request/requestbody/becamerequired/nobreakingchange/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).doesNotHaveAnyElementsOfTypes(RequestBodyBecameRequiredBreakingChange.class);
    }
}
