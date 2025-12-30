package com.docktape.swagger.brake.core.rule.request.parameter.constraint.string;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import org.junit.jupiter.api.Test;

class StringMaxLengthConstraintTest {
    private StringMaxLengthConstraint underTest = new StringMaxLengthConstraint();

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
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
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullNewRequestParamIsGiven() {
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
    void testValidateConstraintsShouldReturnEmptyOptionalWhenRequestParameterIsNotStringTyped() {
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
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMaxLengthIsExtended() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            2,
            1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMaxLengthIsRemoved() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMaxLengthGetsLimited() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            2,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        ConstraintChange expected = new ConstraintChange(
            "maxLength", 2, 1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMaxLengthGetsSet() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            null,
            1
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            1,
            1
        );
        ConstraintChange expected = new ConstraintChange(
            "maxLength", null, 1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }
}