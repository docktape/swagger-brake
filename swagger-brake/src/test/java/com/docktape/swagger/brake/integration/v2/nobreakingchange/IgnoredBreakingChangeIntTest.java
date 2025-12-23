package com.docktape.swagger.brake.integration.v2.nobreakingchange;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Sets;
import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import com.docktape.swagger.brake.runner.Options;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IgnoredBreakingChangeIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testIgnoreBreakingChangeRulesWorksCorrectlyForExactMatch() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdeleted/petstore_v2.yaml";
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setIgnoredBreakingChangeRules(Sets.newHashSet("R004", "R001"));
        // when
        Collection<BreakingChange> result = execute(options);
        // then
        assertThat(result).isEmpty();
    }
}
