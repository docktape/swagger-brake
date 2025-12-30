package com.docktape.swagger.brake.core.model.transformer;

import static org.assertj.core.api.Assertions.assertThat;

import com.docktape.swagger.brake.core.model.Response;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiResponseTransformerTest {
    @Mock
    private MediaTypeTransformer mediaTypeTransformer;

    @InjectMocks
    private ApiResponseTransformer underTest;

    /*
    Totally valid case when the schema only says that the response is 401 and provides only a description without any schema.
     */
    @Test
    void testTransformShouldNotFailWhenFromContentIsNull() {
        // given
        String code = "401";
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setDescription("Unauthorized");
        // when
        Response result = underTest.transform(new ImmutablePair<>(code, apiResponse));
        // then
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(code);
    }
}