package com.docktape.swagger.brake.core.rule.request.parameter.constraint.array;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ArrayConstrainedValue;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import org.junit.jupiter.api.Test;

class ArrayMaxItemsConstraintTest {
    private ArrayMaxItemsConstraint underTest = new ArrayMaxItemsConstraint();

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
    void testValidateConstraintsShouldReturnEmptyOptionalWhenRequestParameterIsNotArrayTyped() {
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
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMaxItemsIsExtended() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
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
    void testValidateConstraintsShouldReturnEmptyOptionalWhenMaxItemsIsRemoved() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            null,
            1,
            false
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMaxItemsGetsLimited() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            2,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ConstraintChange expected = new ConstraintChange(
            "maxItems", 2, 1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }

    @Test
    void testValidateConstraintsShouldReportConstraintChangeWhenMaxItemsGetsSet() {
        // given
        ArrayConstrainedValue oldRequestParameter = new ArrayConstrainedValue(
            null,
            1,
            false
        );
        ArrayConstrainedValue newRequestParameter = new ArrayConstrainedValue(
            1,
            1,
            false
        );
        ConstraintChange expected = new ConstraintChange(
            "maxItems", null, 1
        );
        // when
        Optional<ConstraintChange> result = underTest.validateConstraints(oldRequestParameter, newRequestParameter);
        // then
        assertThat(result).get().isEqualTo(expected);
    }
}