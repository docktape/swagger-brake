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
public class ResponseHeaderBecameOptionalBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final String responseCode;
    private final String headerName;

    @Override
    public String getMessage() {
        return format("%s response header became optional at %s %s for response %s", headerName, method, path, responseCode);
    }

    @Override
    public String getRuleCode() {
        return "R033";
    }
}
