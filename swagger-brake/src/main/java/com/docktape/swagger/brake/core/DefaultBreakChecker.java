package com.docktape.swagger.brake.core;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.BreakingChangeRule;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class DefaultBreakChecker implements BreakChecker {
    private final Collection<BreakingChangeRule<? extends BreakingChange>> rules;

    @Override
    public Collection<BreakingChange> check(Specification oldApi, Specification newApi) {
        if (log.isDebugEnabled()) {
            log.info("Checking for breaking changes using {} rules.", rules.size());
            rules.stream()
                .map(BreakingChangeRule::getClass)
                .map(Class::getName)
                .forEach(name -> log.debug("Rule configured: {}", name));
        }
        if (oldApi == null) {
            throw new IllegalArgumentException("oldApi must be provided");
        }
        if (newApi == null) {
            throw new IllegalArgumentException("newApi must be provided");
        }
        Specification webhookOldSpec = new Specification(oldApi.getWebhooks(), Collections.emptyList(), Collections.emptyList());
        Specification webhookNewSpec = new Specification(newApi.getWebhooks(), Collections.emptyList(), Collections.emptyList());
        return rules.parallelStream()
                .flatMap(rule -> {
                    Collection<? extends BreakingChange> pathChanges = rule.checkRule(oldApi, newApi);
                    Collection<? extends BreakingChange> webhookChanges = rule.checkRule(webhookOldSpec, webhookNewSpec);
                    Collection<BreakingChange> combined = new ArrayList<>(pathChanges);
                    combined.addAll(webhookChanges);
                    return combined.stream();
                })
                .sorted(Comparator.comparing(bc -> bc.getClass().getSimpleName()))
                .collect(toList());
    }
}
