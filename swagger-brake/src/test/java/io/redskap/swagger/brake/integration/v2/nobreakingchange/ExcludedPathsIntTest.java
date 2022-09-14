package io.redskap.swagger.brake.integration.v2.nobreakingchange;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import com.google.common.collect.Sets;
import io.redskap.swagger.brake.core.BreakingChange;
import io.redskap.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import io.redskap.swagger.brake.runner.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ExcludedPathsIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    public void testExcludedPathsWorksCorrectlyForExactMatch() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdeleted/petstore_v2.yaml";
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setExcludedPaths(Sets.newHashSet("/pet/findByStatus"));
        // when
        Collection<BreakingChange> result = execute(options);
        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testExcludedPathsWorksCorrectlyForStartingMatch() {
        // given
        String oldApiPath = "swaggers/v2/request/parameterdeleted/petstore.yaml";
        String newApiPath = "swaggers/v2/request/parameterdeleted/petstore_v2.yaml";
        Options options = new Options();
        options.setOldApiPath(oldApiPath);
        options.setNewApiPath(newApiPath);
        options.setExcludedPaths(Sets.newHashSet("/pet"));
        // when
        Collection<BreakingChange> result = execute(options);
        // then
        assertThat(result).isEmpty();
    }
}
