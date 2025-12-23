package com.docktape.swagger.brake.core.rule.request.parameter.constraint.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import org.junit.jupiter.api.Test;

class ArrayMinItemsConstraintTest {
    private ArrayMinItemsConstraint underTest = new ArrayMinItemsConstraint();

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullOldRequestParamIsGiven() {
        // given
        ArrayConstrainedValue oldRequestParameter = null;
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenNullNewRequestParamIsGiven() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = null;
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenRequestParameterIsNotStringTyped() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMinLengthIsExtended() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            0,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMinLengthIsRemoved() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            null,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinLengthGetsLimited() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            5,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            5,
            2,
            false
        );
        ConstraintChange expected = new ConstraintChange(
            "minItems", 1, 2
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMinLengthGetsSet() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            null,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ConstraintChange expected = new ConstraintChange(
            "minItems", null, 1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }
}