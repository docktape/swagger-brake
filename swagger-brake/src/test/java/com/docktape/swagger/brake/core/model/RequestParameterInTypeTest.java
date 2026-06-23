package com.docktape.swagger.brake.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RequestParameterInTypeTest {

    @Test
    void testFromNameReturnsCookieForCookieString() {
        RequestParameterInType result = RequestParameterInType.fromName("cookie");
        assertThat(result).isEqualTo(RequestParameterInType.COOKIE);
    }

    @Test
    void testFromNameReturnsQueryForQueryString() {
        RequestParameterInType result = RequestParameterInType.fromName("query");
        assertThat(result).isEqualTo(RequestParameterInType.QUERY);
    }

    @Test
    void testFromNameReturnsPathForPathString() {
        RequestParameterInType result = RequestParameterInType.fromName("path");
        assertThat(result).isEqualTo(RequestParameterInType.PATH);
    }

    @Test
    void testFromNameReturnsHeaderForHeaderString() {
        RequestParameterInType result = RequestParameterInType.fromName("header");
        assertThat(result).isEqualTo(RequestParameterInType.HEADER);
    }

    @Test
    void testFromNameThrowsForUnknownName() {
        assertThatThrownBy(() -> RequestParameterInType.fromName("unknown"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
