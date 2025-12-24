package com.docktape.swagger.brake.integration.v2.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.ConstraintChange;
import com.docktape.swagger.brake.core.rule.request.parameter.constraint.RequestParameterConstraintChangedBreakingChange;
import com.docktape.swagger.brake.integration.AbstractSwaggerBrakeIntTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ConstraintChangeIntTest extends AbstractSwaggerBrakeIntTest {
    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumChangesForInteger64() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", new BigDecimal(10), new BigDecimal(5)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumChangesForInteger32() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", new BigDecimal(10), new BigDecimal(5)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
     void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumChangesForIntegerBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", new BigDecimal(10), new BigDecimal(5)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumIntroducedForInteger64() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", null, new BigDecimal(10)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumIntroducedForInteger32() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", null, new BigDecimal(10)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumIntroducedForIntegerBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", null, new BigDecimal(10)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumChangesForInteger64() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", new BigDecimal(1), new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumChangesForInteger32() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", new BigDecimal(1), new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumChangesForIntegerBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", new BigDecimal(1), new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumIntroducedForInteger64() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int64/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", null, new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumIntroducedForInteger32() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/int32/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", null, new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumIntroducedForIntegerBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", null, new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumChangesForDouble() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/double/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/double/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", new BigDecimal("10.0"), new BigDecimal("5.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumChangesForFloat() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/float/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/float/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", new BigDecimal("10.0"), new BigDecimal("5.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumChangesForNumberBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/base/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/base/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", new BigDecimal(10), new BigDecimal(5)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumIntroducedForDouble() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/double/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/double/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", null, new BigDecimal("10.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumIntroducedForFloat() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/float/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/float/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", null, new BigDecimal("10.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumIntroducedForNumberBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/base/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/base/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maximum", null, new BigDecimal(10)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumChangesForDouble() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/double/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/double/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", new BigDecimal("1.0"), new BigDecimal("3.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumChangesForFloat() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/float/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/float/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", new BigDecimal("1.0"), new BigDecimal("3.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumChangesForNumberBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/base/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/base/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", new BigDecimal(1), new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumIntroducedForDouble() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/double/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/double/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", null, new BigDecimal("3.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumIntroducedForFloat() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/float/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/float/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", null, new BigDecimal("3.0")));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinimumIntroducedForNumberBase() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/number/base/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/number/base/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minimum", null, new BigDecimal(3)));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinLengthIntroducedForString() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/string/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/string/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minLength", null, 1));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaxLengthIntroducedForString() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/string/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/string/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maxLength", null, 10));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaxLengthGetsChangedForString() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/string/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/string/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maxLength", 10, 5));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinLengthGetsChangedForString() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/string/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/string/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minLength", 1, 2));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinItemsIntroducedForArray() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/array/newminimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/array/newminimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minItems", null, 1));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaxItemsIntroducedForArray() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/array/newmaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/array/newmaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maxItems", null, 10));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaxItemsGetsChangedForArray() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/array/maximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/array/maximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("maxItems", 10, 5));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMinItemsGetsChangedForArray() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/array/minimum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/array/minimum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("minItems", 1, 2));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenUniqueItemsGetsChangedForArray() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/array/uniqueitems/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/array/uniqueitems/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.GET, "orderId", new ConstraintChange("uniqueItems", false, true));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaxLengthGetsChangedForStringInRequestBody() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/string/requestbodymaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/string/requestbodymaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.POST, "status", new ConstraintChange("maxLength", 10, 8));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaxItemsGetsChangedForArrayInRequestBody() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/array/requestbodymaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/array/requestbodymaximum/swagger-new.json";
        RequestParameterConstraintChangedBreakingChange bc1 = new RequestParameterConstraintChangedBreakingChange("/store/order/{orderId}",
            HttpMethod.POST, "status", new ConstraintChange("maxItems", 10, 8));
        Collection<BreakingChange> expected = Collections.singletonList(bc1);
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result).hasSameElementsAs(expected)
        );
    }

    @Test
    void testRequestTypeConstraintChangeIsBreakingChangeWhenMaximumGetsChangedForIntegerInRequestBody() {
        // given
        String oldApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/requestbodymaximum/swagger-old.json";
        String newApiPath = "swaggers/v2/request/datatypeconstraint/integer/base/requestbodymaximum/swagger-new.json";
        // when
        Collection<BreakingChange> result = execute(oldApiPath, newApiPath);
        // then
        assertThat(result).hasSize(1);
        // for some reason BigDecimal comparison doesn't work in this case, that's why field by field comparison
        RequestParameterConstraintChangedBreakingChange resultConstraintViolation = (RequestParameterConstraintChangedBreakingChange) result.iterator().next();
        ConstraintChange constraintChange = resultConstraintViolation.getConstraintChange();
        BigDecimal oldValue = (BigDecimal) constraintChange.getOldValue();
        BigDecimal newValue = (BigDecimal) constraintChange.getNewValue();

        assertAll(
                () -> assertThat(resultConstraintViolation.getMethod()).isEqualTo(HttpMethod.POST),
                () -> assertThat(resultConstraintViolation.getPath()).isEqualTo("/store/order/{orderId}"),
                () -> assertThat(resultConstraintViolation.getAttributeName()).isEqualTo("status"),
                () -> assertThat(constraintChange.getAttribute()).isEqualTo("maximum"),
                () -> assertThat(oldValue).isEqualByComparingTo(new BigDecimal(10)),
                () -> assertThat(newValue).isEqualByComparingTo(new BigDecimal(8))
        );
    }
}
