package com.docktape.swagger.brake.core.model.transformer;

public interface Transformer<S, R> {
    R transform(S from);
}
