package com.docktape.swagger.brake.integration.v3.schema;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmptySchemaIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testV3EmptySchemaElementFailsStrict() {
        // given
        String oldApiPath = "swaggers/v3/schema/empty-schema/swagger_3.1.json";
        String newApiPath = "swaggers/v3/schema/empty-schema/swagger_3.2.json";

        Throwable exception = catchThrowable(() -> execute(oldApiPath, newApiPath));
        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Schema does not have any type");
    }

    @Test
    void testV3EmptySchemaElementWorksNonStrict() {
        // given
        String oldApiPath = "swaggers/v3/schema/empty-schema/swagger_3.1.json";
        String newApiPath = "swaggers/v3/schema/empty-schema/swagger_3.2.json";

        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);
        // when
        Collection<BreakingChange> result = execute(options);
        // then
        assertThat(result).isEmpty();
    }
}