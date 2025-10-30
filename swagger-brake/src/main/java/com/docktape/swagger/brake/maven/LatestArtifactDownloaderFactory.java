package com.docktape.swagger.brake.maven;

import com.docktape.swagger.brake.runner.Options;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LatestArtifactDownloaderFactory {
    private final ApplicationContext applicationContext;

    /**
     * Factory that constructs a {@link LatestArtifactDownloader} instance.
     * @param options the {@link Options} with which the {@link LatestArtifactDownloader} can be configured.
     * @return the created {@link LatestArtifactDownloader} instance.
     */
    public LatestArtifactDownloader create(Options options) {
        return applicationContext.getBean("maven2LatestArtifactDownloader", LatestArtifactDownloader.class);
    }
}
