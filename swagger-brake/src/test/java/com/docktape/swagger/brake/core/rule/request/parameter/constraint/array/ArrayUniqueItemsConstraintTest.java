package com.docktape.swagger.brake.core.rule.request.parameter.constraint.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import org.junit.jupiter.api.Test;

class ArrayUniqueItemsConstraintTest {
    private ArrayUniqueItemsConstraint underTest = new ArrayUniqueItemsConstraint();

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenUniqueItemsGetsSet() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            null
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "uniqueItems", null, true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenUniqueItemsGetsChangedToTrue() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            true
        );
        ConstraintChange expected = new ConstraintChange(
            "uniqueItems", false, true
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenUniqueItemsGetsSetToFalse() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            null
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenUniqueItemsGetsChangedToFalse() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            true
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }
}