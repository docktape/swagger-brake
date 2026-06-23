package com.docktape.swagger.brake.core.rule.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.docktape.swagger.brake.core.CheckerOptions;
import com.docktape.swagger.brake.core.CheckerOptionsProvider;
import com.docktape.swagger.brake.core.model.Specification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServerUrlChangedRuleTest {
    @Mock
    private CheckerOptionsProvider checkerOptionsProvider;

    @InjectMocks
    private ServerUrlChangedRule underTest;

    @Test
    void testCheckRuleShouldReturnEmptyWhenServerUrlChangeIsDisabled() {
        // given
        CheckerOptions options = new CheckerOptions();
        options.setServerUrlChangeEnabled(false);
        given(checkerOptionsProvider.get()).willReturn(options);
        Specification oldApi = specWithUrls(List.of("https://api.example.com"));
        Specification newApi = specWithUrls(Collections.emptyList());

        // when
        Collection<ServerUrlChangedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testCheckRuleShouldReturnEmptyWhenAllOldUrlsArePresentInNewApi() {
        // given
        CheckerOptions options = new CheckerOptions();
        options.setServerUrlChangeEnabled(true);
        given(checkerOptionsProvider.get()).willReturn(options);
        List<String> urls = Arrays.asList("https://api.example.com", "https://staging.example.com");
        Specification oldApi = specWithUrls(urls);
        Specification newApi = specWithUrls(urls);

        // when
        Collection<ServerUrlChangedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testCheckRuleShouldDetectRemovedServerUrl() {
        // given
        CheckerOptions options = new CheckerOptions();
        options.setServerUrlChangeEnabled(true);
        given(checkerOptionsProvider.get()).willReturn(options);
        Specification oldApi = specWithUrls(Arrays.asList("https://api.example.com", "https://staging.example.com"));
        Specification newApi = specWithUrls(List.of("https://staging.example.com"));

        // when
        Collection<ServerUrlChangedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).extracting(ServerUrlChangedBreakingChange::getOldUrl)
            .containsExactly("https://api.example.com");
    }

    @Test
    void testCheckRuleShouldDetectAllRemovedServerUrls() {
        // given
        CheckerOptions options = new CheckerOptions();
        options.setServerUrlChangeEnabled(true);
        given(checkerOptionsProvider.get()).willReturn(options);
        Specification oldApi = specWithUrls(Arrays.asList("https://api.example.com", "https://staging.example.com"));
        Specification newApi = specWithUrls(Collections.emptyList());

        // when
        Collection<ServerUrlChangedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(ServerUrlChangedBreakingChange::getOldUrl)
            .containsExactlyInAnyOrder("https://api.example.com", "https://staging.example.com");
    }

    @Test
    void testCheckRuleShouldReturnEmptyWhenOldApiHasNoServers() {
        // given
        CheckerOptions options = new CheckerOptions();
        options.setServerUrlChangeEnabled(true);
        given(checkerOptionsProvider.get()).willReturn(options);
        Specification oldApi = specWithUrls(Collections.emptyList());
        Specification newApi = specWithUrls(List.of("https://api.example.com"));

        // when
        Collection<ServerUrlChangedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testCheckRuleShouldNotFlagAddedServerUrls() {
        // given
        CheckerOptions options = new CheckerOptions();
        options.setServerUrlChangeEnabled(true);
        given(checkerOptionsProvider.get()).willReturn(options);
        Specification oldApi = specWithUrls(List.of("https://api.example.com"));
        Specification newApi = specWithUrls(Arrays.asList("https://api.example.com", "https://new.example.com"));

        // when
        Collection<ServerUrlChangedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).isEmpty();
    }

    private Specification specWithUrls(List<String> urls) {
        return new Specification(Collections.emptyList(), urls);
    }
}
