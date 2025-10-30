package com.docktape.swagger.brake.integration.v2.beta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.beta.StandardApiToBetaApiBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BetaApiIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testBetaApiAdditionDoesntTriggerABreak() {
        // given
        String oldApiPath = "swaggers/v2/beta/betaapiadded/petstore.yaml";
        String newApiPath = "swaggers/v2/beta/betaapiadded/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testBetaApiModificationDoesntTriggerABreak() {
        // given
        String oldApiPath = "swaggers/v2/beta/betaapimodified/petstore.yaml";
        String newApiPath = "swaggers/v2/beta/betaapimodified/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testBetaApiModificationDoesntTriggerABreakWhenUsingCustomAttribute() {
        // given
        String oldApiPath = "swaggers/v2/beta/customextensionname/petstore.yaml";
        String newApiPath = "swaggers/v2/beta/customextensionname/petstore_v2.yaml";

        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setBetaApiExtensionName("x-custom-beta-attribute");
        // when
        Collection<BreakingChange> result = execute(options);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testBetaApiToStandardApiDoesntTriggerABreak() {
        // given
        String oldApiPath = "swaggers/v2/beta/betaapitostandard/petstore.yaml";
        String newApiPath = "swaggers/v2/beta/betaapitostandard/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testBetaApiRemovalDoesntTriggerABreak() {
        // given
        String oldApiPath = "swaggers/v2/beta/betaapiremoved/petstore.yaml";
        String newApiPath = "swaggers/v2/beta/betaapiremoved/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testStandardApiToBetaApiTriggersABreak() {
        // given
        String oldApiPath = "swaggers/v2/beta/standardapitobeta/petstore.yaml";
        String newApiPath = "swaggers/v2/beta/standardapitobeta/petstore_v2.yaml";
        BreakingChange bc1 = new StandardApiToBetaApiBreakingChange("/pet", HttpMethod.POST);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).containsExactly(bc1);
    }
}
