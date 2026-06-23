package com.docktape.swagger.brake.integration.v3.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.path.PathDeletedBreakingChange;
import com.docktape.swagger.brake.core.rule.response.ResponseTypeChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class WebhookBreakingChangeIntTest extends AbstractSwaggerBrakeIntTest {

    @Test
    void testRemovedWebhookOperationIsDetected() {
        String oldApiPath = "swaggers/v3/webhook/deleted/api_v1.yaml";
        String newApiPath = "swaggers/v3/webhook/deleted/api_v2.yaml";
        PathDeletedBreakingChange expectedChange = new PathDeletedBreakingChange("newPet", HttpMethod.POST);
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);

        Collection<BreakingChange> result = execute(options);

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(expectedChange)
        );
    }

    @Test
    void testChangedWebhookResponseTypeIsDetected() {
        String oldApiPath = "swaggers/v3/webhook/response-type-changed/api_v1.yaml";
        String newApiPath = "swaggers/v3/webhook/response-type-changed/api_v2.yaml";
        ResponseTypeChangedBreakingChange expectedChange =
            new ResponseTypeChangedBreakingChange("petUpdated", HttpMethod.POST, "200", "id", "integer", "string");
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setStrictValidation(false);

        Collection<BreakingChange> result = execute(options);

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(expectedChange)
        );
    }
}
