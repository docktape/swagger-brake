package com.docktape.swagger.brake.core.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Specification {
    private final Collection<Path> paths;
    private final List<String> serverUrls;

    public Optional<Path> getPath(Path path) {
        return getPath(path.getPath(), path.getMethod());
    }

    public Optional<Path> getPath(String path, HttpMethod method) {
        return paths.stream().filter(p -> path.equals(p.getPath())).filter(p -> method.equals(p.getMethod())).findAny();
    }
}
