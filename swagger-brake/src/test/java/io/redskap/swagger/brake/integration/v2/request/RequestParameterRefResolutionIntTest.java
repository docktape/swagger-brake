package io.redskap.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import io.redskap.swagger.brake.core.BreakingChange;
import io.redskap.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class RequestParameterRefResolutionIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testRequestParameterRefResolutionWorks() {
        // given
        String apiPath = "swaggers/v2/request/refresolution/petstore.yaml";

        // when
        Collection<BreakingChange> result = execute(apiPath, apiPath);
        // then
        assertThat(result).isEmpty();
    }
}