package com.docktape.swagger.brake.maven.jar.filename;

import java.util.Collection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ApiFileNameCheckerChain implements ApiFileNameChecker {
    private final Collection<ApiFileNameChecker> checkers;

    @Override
    public boolean isApiFile(String fileName) {
        for (ApiFileNameChecker checker : checkers) {
            if (checker.isApiFile(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
