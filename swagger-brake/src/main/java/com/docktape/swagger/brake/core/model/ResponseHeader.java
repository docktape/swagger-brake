package com.docktape.swagger.brake.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseHeader {
    private final String name;
    private final boolean required;
    private final String type;
}
