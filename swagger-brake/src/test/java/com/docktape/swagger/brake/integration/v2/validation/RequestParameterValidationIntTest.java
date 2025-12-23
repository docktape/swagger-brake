package com.docktape.swagger.brake.integration.v2.validation;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;

@ExtendWith(SpringExtension.class)
class RequestParameterValidationIntTest extends AbstractSwaggerBrakeIntTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "swaggers/v2/validation/requestparam/header_swagger_missing.json",
        "swaggers/v2/validation/requestparam/query_swagger_missing.json",
        "swaggers/v2/validation/requestparam/path_swagger_missing.json"
    })
    void testValidationErrorHappensWhenParameterMissingTypeStrict(String apiPath) {
        // given
        // when
        Throwable exception = catchThrowable(() -> execute(apiPath, apiPath));
        // then
        assertThat(exception)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Schema does not have any type");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "swaggers/v2/validation/requestparam/header_swagger_missing.json",
        "swaggers/v2/validation/requestparam/query_swagger_missing.json",
        "swaggers/v2/validation/requestparam/path_swagger_missing.json"
    })
    void testValidationErrorHappensWhenParameterMissingTypeNonStrict(String apiPath) {
        // given
        // when
        Collection<BreakingChange> result = execute(createOptions(apiPath, apiPath));
        // then
        assertThat(result).isNotNull().isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "swaggers/v2/validation/requestparam/header_swagger.json",
        "swaggers/v2/validation/requestparam/query_swagger.json",
        "swaggers/v2/validation/requestparam/path_swagger.json"
    })
    void testValidationSucceedsWithValidParameter(String apiPath) {
        // given - valid Swagger 2.0 spec with parameter using direct type property
        // when
        Collection<BreakingChange> result = execute(apiPath, apiPath);

        // then - valid spec should be processed successfully
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void testStrictValidationThrowsExceptionForInvalidParameterSchema() {
        // given - invalid Swagger 2.0 spec using schema wrapper for non-body parameter
        String oldApiPath = "swaggers/v2/validation/requestparam/header_swagger_invalid.json";
        String newApiPath = "swaggers/v2/validation/requestparam/header_swagger_invalid.json";
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(true);
        
        // when - strict mode validation encounters schema with null type
        Throwable result = catchThrowable(() -> execute(options));
        
        // then - should throw IllegalStateException with appropriate message
        assertAll(
            () -> assertThat(result).isInstanceOf(IllegalStateException.class),
            () -> assertThat(result.getMessage()).contains("Schema does not have any type"),
            () -> assertThat(result.getMessage()).contains("Schema has null type")
        );
    }

    private Options createOptions(String oldApiPath, String newApiPath) {
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);
        return options;
    }
}
