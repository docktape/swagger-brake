package com.docktape.swagger.brake.integration.v3.schema;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class EmptySchemaIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testV3EmptySchemaElementWorks() {
        // given
        String oldApiPath = "swaggers/v3/schema/empty-schema/swagger_3.1.json";
        String newApiPath = "swaggers/v3/schema/empty-schema/swagger_3.2.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(0);
    }
}