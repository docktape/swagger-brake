package com.docktape.swagger.brake.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Specification {
    private final Collection<Path> paths;
    private final List<String> serverUrls;
    private final Collection<Path> webhooks;

    public Specification(Collection<Path> paths, List<String> serverUrls, Collection<Path> webhooks) {
        this.paths = paths != null ? paths : Collections.emptyList();
        this.serverUrls = serverUrls != null ? serverUrls : Collections.emptyList();
        this.webhooks = webhooks != null ? webhooks : Collections.emptyList();
    }

    public Specification(Collection<Path> paths, List<String> serverUrls) {
        this(paths, serverUrls, Collections.emptyList());
    }

    public Specification(Collection<Path> paths) {
        this(paths, Collections.emptyList(), Collections.emptyList());
    }

    public Collection<Path> getAllPaths() {
        Collection<Path> all = new ArrayList<>(paths);
        all.addAll(webhooks);
        return all;
    }

    public Optional<Path> getPath(Path path) {
        return getPath(path.getPath(), path.getMethod());
    }

    public Optional<Path> getPath(String path, HttpMethod method) {
        return paths.stream().filter(p -> path.equals(p.getPath())).filter(p -> method.equals(p.getMethod())).findAny();
    }
}
