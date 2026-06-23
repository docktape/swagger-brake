package com.docktape.swagger.brake.core.rule.response;

import static java.lang.String.format;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ResponseMediaTypeGeneralizedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String responseCode;
    private final String oldMediaType;
    private final String newMediaType;

    @Override
    public String getMessage() {
        return format("Response media type %s was generalized to %s at %s %s %s",
            oldMediaType, newMediaType, method, path, responseCode);
    }

    @Override
    public String getRuleCode() {
        return "R034";
    }
}
