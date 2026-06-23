package com.docktape.swagger.brake.core.rule.request.parameter.constraint.number;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import org.junit.jupiter.api.Test;

class NumberMultipleOfConstraintTest {
    private NumberMultipleOfConstraint underTest = new NumberMultipleOfConstraint();

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
        // given
        NumberConstrainedValue oldRequestParameter = null;
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullNewRequestParamIsGiven() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        NumberConstrainedValue newRequestParameter = null;
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenBothMultipleOfAreNull() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null, null, false, false, null
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null, null, false, false, null
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportBreakingChangeWhenMultipleOfIsIntroduced() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null, null, false, false, null
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        ConstraintChange expected = new ConstraintChange("multipleOf", null, new BigDecimal("5"));
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAttribute()).isEqualTo(expected.getAttribute());
        assertThat(result.get().getOldValue()).isEqualTo(expected.getOldValue());
    }

    @Test
    void testValidateConstraintsShouldReportBreakingChangeWhenMultipleOfChanges() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("3")
        );
        ConstraintChange expected = new ConstraintChange("multipleOf", new BigDecimal("5"), new BigDecimal("3"));
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAttribute()).isEqualTo(expected.getAttribute());
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMultipleOfIsRemoved() {
        // given - removing multipleOf is a loosening, not a breaking change
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null, null, false, false, null
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMultipleOfIsUnchanged() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null, null, false, false, new BigDecimal("5")
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }
}
