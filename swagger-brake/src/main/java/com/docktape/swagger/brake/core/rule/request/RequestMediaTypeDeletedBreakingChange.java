package com.docktape.swagger.brake.core.rule.request;

import static java.lang.String.format;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.model.MediaType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestMediaTypeDeletedBreakingChange implements BreakingChange {
    private final String path;
    private final HttpMethod method;
    private final MediaType mediaType;

    @Override
    public String getMessage() {
        return format("%s media type request was removed from %s %s", mediaType, method, path);
    }

    @Override
    public String getRuleCode() {
        return "R003";
    }
}
