package com.docktape.swagger.brake.core;

import java.util.Collection;

import com.docktape.swagger.brake.core.model.Specification;

public interface BreakChecker {
    Collection<BreakingChange> check(Specification oldApi, Specification newApi);
}
