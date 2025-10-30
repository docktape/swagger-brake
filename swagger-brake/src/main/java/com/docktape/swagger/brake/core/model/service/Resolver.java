package com.docktape.swagger.brake.core.model.service;

public interface Resolver<S, R> {
    R resolve(S from);
}
