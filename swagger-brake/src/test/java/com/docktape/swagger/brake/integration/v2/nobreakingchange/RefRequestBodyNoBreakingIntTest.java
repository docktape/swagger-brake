package com.docktape.swagger.brake.integration.v2.nobreakingchange;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RefRequestBodyNoBreakingIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testNoBreakingChangeWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/nobreakingchange/refrequestbody/schema.yaml";
        String newApiPath = "swaggers/v2/nobreakingchange/refrequestbody/schema_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }
}
