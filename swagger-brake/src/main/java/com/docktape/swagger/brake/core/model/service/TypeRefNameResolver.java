package com.docktape.swagger.brake.core.model.service;

import org.springframework.stereotype.Component;

@Component
public class TypeRefNameResolver implements Resolver<String, String> {
    @Override
    public String resolve(String from) {
        return getNameFromRef(from);
    }

    private String getNameFromRef(String refName) {
        if (refName == null) {
            return null;
        }
        return refName.substring(refName.lastIndexOf("/") + 1);
    }
}
