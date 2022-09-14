package io.redskap.swagger.brake.core.rule.request.parameter.constraint.string;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import io.redskap.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import io.redskap.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import org.junit.jupiter.api.Test;

public class StringMinLengthConstraintTest {
    private StringMinLengthConstraint underTest = new StringMinLengthConstraint();

    @Test
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
        // given
        StringConstrainedValue oldRequestParameter = null;
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenNullNewRequestParamIsGiven() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        StringConstrainedValue newRequestParameter = null;
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenRequestParameterIsNotStringTyped() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenMinLengthIsExtended() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            0
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenMinLengthIsRemoved() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            null
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMinLengthGetsLimited() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            5,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            5,
            2
        );
        ConstraintChange expected = new ConstraintChange(
            "minLength", 1, 2
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMinLengthGetsSet() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            null
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        ConstraintChange expected = new ConstraintChange(
            "minLength", null, 1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }
}