package com.docktape.swagger.brake.core.rule.server;

import com.docktape.swagger.brake.core.BreakingChange;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ServerUrlChangedBreakingChange implements BreakingChange {
    private final String oldUrl;

    @Override
    public String getMessage() {
        return String.format("Server URL %s was removed or changed", oldUrl);
    }

    @Override
    public String getRuleCode() {
        return "R035";
    }
}
