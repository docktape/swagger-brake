package io.redskap.swagger.brake.integration.v2.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;

import io.redskap.swagger.brake.core.BreakingChange;
import io.redskap.swagger.brake.core.model.HttpMethod;
import io.redskap.swagger.brake.core.rule.path.PathDeletedBreakingChange;
import io.redskap.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import io.redskap.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PathDeletionIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testPathDeletionWorksCorrectly() {
        // given
        String oldApiPath = "swaggers/v2/path/deleted/petstore.yaml";
        String newApiPath = "swaggers/v2/path/deleted/petstore_v2.yaml";
        PathDeletedBreakingChange bc = new PathDeletedBreakingChange("/pet/findByStatus", HttpMethod.GET);
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }

    @Test
    public void testPathDeletionDoesntTriggerWhenDeprecated() {
        // given
        String oldApiPath = "swaggers/v2/path/deleted/deprecated/petstore.yaml";
        String newApiPath = "swaggers/v2/path/deleted/deprecated/petstore_v2.yaml";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testPathDeletionTriggeredWhenDeprecatedAndDeprecationIsNotAllowed() {
        // given
        String oldApiPath = "swaggers/v2/path/deleted/deprecated/petstore.yaml";
        String newApiPath = "swaggers/v2/path/deleted/deprecated/petstore_v2.yaml";
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setDeprecatedApiDeletionAllowed(false);
        PathDeletedBreakingChange bc = new PathDeletedBreakingChange("/pet/findByStatus", HttpMethod.GET);
        Collection<BreakingChange> expected = Collections.singleton(bc);
        // when
        Collection<BreakingChange> result = execute(options);
        // then
        assertThat(result).hasSize(1);
        assertThat(result).hasSameElementsAs(expected);
    }
}