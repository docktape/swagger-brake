package com.docktape.swagger.brake.core.rule.request.parameter.constraint.number;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import org.junit.jupiter.api.Test;

class NumberMinimumConstraintTest {
    private NumberMinimumConstraint underTest = new NumberMinimumConstraint();

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
        // given
        NumberConstrainedValue oldRequestParameter = null;
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            true,
            true
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
            BigDecimal.ONE,
            BigDecimal.ZERO,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = null;
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenRequestParameterIsNotIntegerTyped() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            true,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMinimumValueSetIsExtended() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE,
            true,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMinimumValueSetIsExtendedWithExclusiveMaximumSetting() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            false,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetIsLimitedWithExclusiveMaximumSetting() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            false,
            false
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            true,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "exclusiveMinimum", false, true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetIsLimitedWithExclusiveMaximumSettingEdgeCase1() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN.subtract(BigDecimal.ONE),
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            false,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetIsLimitedWithExclusiveMaximumSettingEdgeCase2() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN,
            false,
            false
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.TEN.subtract(BigDecimal.ONE),
            true,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetGetsLimitedForInt64() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.TEN,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", BigDecimal.ONE, BigDecimal.TEN
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetGetsLimitedForInt32() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.TEN,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", BigDecimal.ONE, BigDecimal.TEN
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetGetsLimitedForInteger() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.TEN,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", BigDecimal.ONE, BigDecimal.TEN
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetGetsLimitedForNumber() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.TEN,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", BigDecimal.ONE, BigDecimal.TEN
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetGetsLimitedForFloat() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE.subtract(new BigDecimal("0.5")),
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.TEN.subtract(new BigDecimal("0.5")),
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", BigDecimal.ONE.subtract(new BigDecimal("0.5")), BigDecimal.TEN.subtract(new BigDecimal("0.5"))
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueSetGetsLimitedForDouble() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ONE.subtract(new BigDecimal("0.5")),
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.TEN.subtract(new BigDecimal("0.5")),
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", BigDecimal.ONE.subtract(new BigDecimal("0.5")), BigDecimal.TEN.subtract(new BigDecimal("0.5"))
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueGetsSet() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null,
            null,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ONE,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "minimum", null, BigDecimal.ONE
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinimumValueGetsRemoved() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null,
            null,
            false,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }
}