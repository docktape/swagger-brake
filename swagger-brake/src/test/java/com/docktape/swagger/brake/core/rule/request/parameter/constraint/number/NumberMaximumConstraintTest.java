package com.docktape.swagger.brake.core.rule.request.parameter.constraint.number;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.NumberConstrainedValue;
import org.junit.jupiter.api.Test;

public class NumberMaximumConstraintTest {
    private NumberMaximumConstraint underTest = new NumberMaximumConstraint();

    @Test
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
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
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenNullNewRequestParamIsGiven() {
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
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenMaximumValueSetIsExtended() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
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
    public void testValidateConstraintsShouldReturnEmptyOptionalWhenMaximumValueSetIsExtendedWithExclusiveMaximumSetting() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetIsLimitedWithExclusiveMaximumSetting() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            true,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "exclusiveMaximum", false, true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetIsLimitedWithExclusiveMaximumSettingEdgeCase1() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN.add(BigDecimal.ONE),
            BigDecimal.ZERO,
            true,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetIsLimitedWithExclusiveMaximumSettingEdgeCase2() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN.add(BigDecimal.ONE),
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
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetGetsLimitedForInt64() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", BigDecimal.TEN, BigDecimal.ONE
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetGetsLimitedForInt32() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", BigDecimal.TEN, BigDecimal.ONE
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetGetsLimitedForInteger() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", BigDecimal.TEN, BigDecimal.ONE
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetGetsLimitedForNumber() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", BigDecimal.TEN, BigDecimal.ONE
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetGetsLimitedForFloat() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN.add(new BigDecimal("0.5")),
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE.add(new BigDecimal("0.5")),
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", BigDecimal.TEN.add(new BigDecimal("0.5")), BigDecimal.ONE.add(new BigDecimal("0.5"))
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueSetGetsLimitedForDouble() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.TEN.add(new BigDecimal("0.5")),
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE.add(new BigDecimal("0.5")),
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", BigDecimal.TEN.add(new BigDecimal("0.5")), BigDecimal.ONE.add(new BigDecimal("0.5"))
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueGetsSet() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            null,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "maximum", null, BigDecimal.ONE
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    public void testValidateConstraintsShouldReportConstraintChangeWhenMaximumValueGetsRemoved() {
        // given
        NumberConstrainedValue oldRequestParameter = new NumberConstrainedValue(
            BigDecimal.ONE,
            BigDecimal.ZERO,
            false,
            true
        );
        NumberConstrainedValue newRequestParameter = new NumberConstrainedValue(
            null,
            BigDecimal.ZERO,
            false,
            true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }
}