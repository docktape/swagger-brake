package com.docktape.swagger.brake.cli.options.handler;

import com.docktape.swagger.brake.cli.options.CliOption;
import com.docktape.swagger.brake.runner.Options;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handler for the strict validation CLI option.
 * When enabled (default), malformed OpenAPI specifications will cause the tool to fail with an exception.
 * When disabled, the tool will log warnings instead of failing.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StrictValidationHandler implements CliOptionHandler {

    @Override
    public void handle(String optionValue, Options options) {
        if (optionValue == null || "true".equalsIgnoreCase(optionValue)) {
            options.setStrictValidation(true);
            log.debug("Strict validation option being used: {}", options);
            return;
        }
        options.setStrictValidation(false);
        log.debug("Non-Strict validation option being used: {}", options);
    }

    @Override
    public CliOption getHandledCliOption() {
        return CliOption.STRICT_VALIDATION;
    }

    @Override
    public String getHelpMessage() {
        return "Strict validation mode: When enabled (Default), malformed OpenAPI specifications will cause the tool to fail with an exception instead of logging warnings.";
    }
}
