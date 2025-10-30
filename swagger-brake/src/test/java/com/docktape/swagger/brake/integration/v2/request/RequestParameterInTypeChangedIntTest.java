package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.RequestParameterInTypeChangedBreakingChange;
import com.docktape.swagger.brake.core.rule.request.RequestTypeAttributeRemovedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RequestParameterInTypeChangedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testRequestParameterInTypeChangedWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterintypechanged/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterintypechanged/petstore_v2.yaml";
        RequestParameterInTypeChangedBreakingChange bc = new RequestParameterInTypeChangedBreakingChange("/pet/findByStatus", HttpMethod.GET, "status", "query", "header");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testBreakingChangeWhenFormDataRequestParamIsChangedToHeader() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterintypechanged/formData_swagger.json";
        String newApiPath = "swaggers/v2/request/parameterintypechanged/formData_swagger_v2.json";
        RequestTypeAttributeRemovedBreakingChange bc = new RequestTypeAttributeRemovedBreakingChange("/file/external", HttpMethod.POST, "access_token");
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }
}
