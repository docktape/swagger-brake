package com.docktape.swagger.brake.integration.project;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.ImageFromDockerfile;

@Slf4j
public abstract class ProjectContainerBase<T extends ProjectParameter> {
    protected GenericContainer container;

    protected ProjectContainerBase(Network network, T parameter) {
        this.container = createContainer(network, parameter);
    }

    private GenericContainer createContainer(Network network, T parameter) {
        return configureContainer(
            new GenericContainer<>(containerImage(parameter))
                .withNetwork(network)
                .withEnv("CUSTOM_VERSION", parameter.getCustomVersion())
                .withEnv("ARTIFACTORY_CONTEXT_URL", parameter.getArtifactoryContextUrl())
                .withEnv("ARTIFACTORY_USERNAME", parameter.getUsername())
                .withEnv("ARTIFACTORY_PASSWORD", parameter.getPassword())
                .withEnv("ARTIFACTORY_REPO_KEY", parameter.getReleaseRepoKey())
                .withEnv("ARTIFACTORY_REPO_URL", parameter.getReleaseRepoUrl())
                .withEnv("ARTIFACTORY_SNAPSHOT_REPO_KEY", parameter.getSnapshotRepoKey())
                .withEnv("ARTIFACTORY_SNAPSHOT_REPO_URL", parameter.getSnapshotRepoUrl())
                .withEnv("PACKAGING", parameter.getPackagingType().getPackaging()),
            parameter);
    }

    public void build() {
        log.info("Starting to build the example project..");
        container.start();
        waitUntilBuildIsComplete();
        log.info("Example project build is complete");
    }

    public void buildAsync(CountDownLatch latch) {
        container.start();
        new Thread(new ProjectContainerBase.ContainerWatcher(container, this::isBuildComplete, latch::countDown)).start();
    }

    private void waitUntilBuildIsComplete() {
        try {
            Awaitility.await()
                .atMost(Duration.ofMinutes(10))
                .pollInterval(Duration.ofSeconds(5))
                .until(() -> {
                    String logs = container.getLogs();
                    log.info("Waiting for build to complete..");
                    return isBuildComplete(logs);
                });
        } catch (ConditionTimeoutException e) {
            log.error("{}", container.getLogs());
            throw e;
        }
    }

    public boolean noBreakingApi() {
        return container.getLogs().contains("No breaking API changes detected");
    }

    public boolean isApiBrokenWith(String text) {
        return container.getLogs().contains("There were breaking API changes") && container.getLogs().contains(text);
    }

    protected boolean isBuildComplete(String logs) {
        boolean isBuildSuccess = isBuildSuccess(logs);
        if (isBuildFailure(logs) || (!isBuildSuccess && !container.isRunning())) {
            throw new BuildFailureException(logs);
        }
        return isBuildSuccess;
    }

    public String getLogs() {
        return container.getLogs();
    }

    protected abstract ImageFromDockerfile containerImage(T parameter);

    protected GenericContainer configureContainer(GenericContainer container, T parameter) {
        return container;
    }

    protected abstract boolean isBuildSuccess(String logs);

    protected abstract boolean isBuildFailure(String logs);

    @RequiredArgsConstructor
    @Slf4j
    private static class ContainerWatcher implements Runnable {
        private final GenericContainer container;
        private final Function<String, Boolean> logWatcherFunction;
        private final Runnable completenessListener;

        @Override
        public void run() {
            log.info("Watching the build to complete..");
            Awaitility.await().atMost(Duration.ofMinutes(2)).pollInterval(Duration.ofSeconds(5)).until(() -> logWatcherFunction.apply(container.getLogs()));
            log.info("Build has been completed..");
            completenessListener.run();
        }
    }
}
