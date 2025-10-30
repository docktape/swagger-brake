package com.docktape.swagger.brake.core.rule;

import java.util.Collection;

import com.docktape.swagger.brake.core.BreakingChange;
import com.docktape.swagger.brake.core.model.Specification;

public interface BreakingChangeRule<T extends BreakingChange> {
    Collection<T> checkRule(Specification oldApi, Specification newApi);
}
