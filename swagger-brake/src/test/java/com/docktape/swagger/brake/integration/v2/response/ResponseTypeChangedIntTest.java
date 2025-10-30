package com.docktape.swagger.brake.integration.v2.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.response.ResponseTypeChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ResponseTypeChangedIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testResponseTypeChangeIsBreakingChangeWhenExistingAttributeRemoved() {
        // given
        String oldApiPath = "swaggers/v2/response/typechanged/petstore.yaml";
        String newApiPath = "swaggers/v2/response/typechanged/petstore_v2.yaml";
        ResponseTypeChangedBreakingChange bc1 = new ResponseTypeChangedBreakingChange("/pet/findByStatus", HttpMethod.GET, "200", "", "array", "object");
        ResponseTypeChangedBreakingChange bc2 = new ResponseTypeChangedBreakingChange("/pet/findByTags", HttpMethod.GET, "200", "", "array", "string");
        Collection<BreakingChange> expected = Arrays.asList(bc1, bc2);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(2);
        assertThat(result).hasSameElementsAs(expected);
    }
}
