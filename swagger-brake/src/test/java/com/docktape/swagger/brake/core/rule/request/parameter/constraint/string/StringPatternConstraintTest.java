package com.docktape.swagger.brake.core.rule.request.parameter.constraint.string;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.StringConstrainedValue;
import org.junit.jupiter.api.Test;

class StringPatternConstraintTest {
    private StringPatternConstraint underTest = new StringPatternConstraint();

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
        // given
        StringConstrainedValue oldRequestParameter = null;
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[a-z]+"
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
            null,
            null,
            "[a-z]+"
        );
        StringConstrainedValue newRequestParameter = null;
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenPatternIsNotChanged() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[a-z]+"
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[a-z]+"
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenBothPatternsAreNull() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            null,
            null,
            null
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            null,
            null
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenPatternIsRemoved() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[a-z]+"
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            null,
            null
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenPatternIsAdded() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            null,
            null,
            null
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[a-z]+"
        );
        ConstraintChange expected = new ConstraintChange(
            "pattern", null, "[a-z]+"
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenPatternIsChanged() {
        // given
        StringConstrainedValue oldRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[a-z]+"
        );
        StringConstrainedValue newRequestParameter = new StringConstrainedValue(
            null,
            null,
            "[A-Z]+"
        );
        ConstraintChange expected = new ConstraintChange(
            "pattern", "[a-z]+", "[A-Z]+"
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }
}
