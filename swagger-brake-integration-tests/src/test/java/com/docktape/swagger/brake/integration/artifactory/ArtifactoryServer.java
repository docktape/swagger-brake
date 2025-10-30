package com.docktape.swagger.brake.integration.artifactory;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import com.google.gson.Gson;
import com.docktape.swagger.brake.integration.artifactory.factory.PageFactory;
import com.docktape.swagger.brake.integration.artifactory.factory.WebDriverFactory;
import com.docktape.swagger.brake.integration.artifactory.page.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

@Slf4j
public class ArtifactoryServer {
    private static final Duration STARTUP_TIMEOUT = Duration.ofMinutes(5);

    public static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "password";
    public static final String UPDATED_PASSWORD = "artifactory";
    public static final String SNAPSHOT_REPO_KEY = "libs-snapshot-local";
    public static final String RELEASE_REPO_KEY = "libs-release-local";

    private final HttpClient httpClient = HttpClientBuilder.create().build();
    private final Gson gson = new Gson();
    private final GenericContainer artifactoryContainer;
    private final Network dockerNetwork;

    public ArtifactoryServer(Network dockerNetwork) {
        this.artifactoryContainer = new GenericContainer<>("docker.bintray.io/jfrog/artifactory-oss:7.5.7")
            .withExposedPorts(8081, 8082)
            .withNetwork(dockerNetwork)
            .withStartupTimeout(STARTUP_TIMEOUT);
        this.dockerNetwork = dockerNetwork;
    }

    public void initialize() {
        log.info("Starting up Artifactory container");
        artifactoryContainer.start();
        waitUntilArtifactoryIsInitialized();
        log.info("Artifactory started");
        log.info("Starting to initialize Artifactory");
        WebDriver webDriver = WebDriverFactory.create(((Network.NetworkImpl) dockerNetwork).getName());
        PageFactory pageFactory = new PageFactory(webDriver);
        webDriver.get(getBaseUiInternalUrl());

        log.info("Logging into Artifactory");
        ArtifactoryLoginPage loginPage = pageFactory.create(ArtifactoryLoginPage.class);
        loginPage.waitForPageLoad();
        loginPage.enterCredentials(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        loginPage.submitCredentials();

        ArtifactoryInitializerPage initializerPage = pageFactory.create(ArtifactoryInitializerPage.class);
        initializerPage.waitForPageLoad();
        log.info("Login successful, going through the get started screens");
        initializerPage.clickGetStarted();
        initializerPage.enterNewPassword(UPDATED_PASSWORD);
        initializerPage.next();
        initializerPage.waitFor(1);
        initializerPage.skip();
        initializerPage.waitFor(1);
        initializerPage.skip();
        initializerPage.waitFor(1);
        initializerPage.next();
        initializerPage.waitFor(1);
        initializerPage.finish();
        initializerPage.waitFor(1);
        log.info("Get started screens complete");

        ArtifactoryMainPage mainPage = pageFactory.create(ArtifactoryMainPage.class);
        mainPage.openAdminPanel();
        ArtifactoryRepositoriesPage repositoriesPage = mainPage.openRepositoriesSettings();
        ArtifactoryNewRepositoryPage newSnapshotRepositoryPage = repositoriesPage.newLocalRepository();
        newSnapshotRepositoryPage
            .selectMavenRepository()
            .enterRepoKey(SNAPSHOT_REPO_KEY)
            .uncheckHandleReleases()
            .saveAndFinish();
        log.info("Created {} repo", SNAPSHOT_REPO_KEY);
        repositoriesPage.waitFor(2);
        ArtifactoryNewRepositoryPage newReleaseRepositoryPage = repositoriesPage.newLocalRepository();
        newReleaseRepositoryPage
            .selectMavenRepository()
            .enterRepoKey(RELEASE_REPO_KEY)
            .uncheckHandleSnapshots()
            .saveAndFinish();
        log.info("Created {} repo", RELEASE_REPO_KEY);
        repositoriesPage.waitFor(2);
        webDriver.close();
        log.info("Initialization complete");
        log.info("Artifactory API is accessible at {}", getBaseApiUrl());
        log.info("Artifactory UI is accessible at {}", getBaseUiUrl());
    }

    public void resetRepos() {
        resetRepo(SNAPSHOT_REPO_KEY);
        resetRepo(RELEASE_REPO_KEY);
    }

    public void resetRepo(String repoKey) {
        log.info("Resetting {}", repoKey);
        String listUri = format("%s/api/storage/%s", getBaseApiUrl(), repoKey);
        HttpUriRequest listRequest = new HttpGet(toUri(listUri));
        listRequest.addHeader(getAuthorizationHeader());
        try {
            log.info("Getting list of artifacts under {}", repoKey);
            HttpResponse listResponse = httpClient.execute(listRequest);
            int listStatusCode = listResponse.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(listResponse.getEntity());
            if (listStatusCode != HttpStatus.SC_OK) {
                log.error(content);
                throw new RuntimeException("Unable to list repo " + repoKey);
            }
            ListResponse listResponsePojo = gson.fromJson(content, ListResponse.class);
            log.info("Found {} artifact folders under {}", listResponsePojo.getChildren().size(), repoKey);
            listResponsePojo.getChildren().forEach(child -> deleteRepoChild(repoKey, child.getUri()));
            log.info("Artifacts under {} were deleted", repoKey);
        } catch (IOException e) {
            throw new RuntimeException("Unable to list repo " + repoKey);
        }
    }

    private void deleteRepoChild(String repoKey, String child) {
        log.info("Deleting {} from {}", child, repoKey);
        String deleteUri = format("%s/%s/%s", getBaseApiUrl(), repoKey, child);
        HttpUriRequest deleteRequest = new HttpDelete(toUri(deleteUri));
        deleteRequest.addHeader(getAuthorizationHeader());

        try {
            HttpResponse deleteResponse = httpClient.execute(deleteRequest);
            int deleteStatusCode = deleteResponse.getStatusLine().getStatusCode();
            if (deleteStatusCode != HttpStatus.SC_NO_CONTENT) {
                log.error(EntityUtils.toString(deleteResponse.getEntity()));
                throw new RuntimeException("Error while resetting " + repoKey);
            }
            log.info("Deletion of {} under {} is complete", child, repoKey);
        } catch (IOException e) {
            throw new RuntimeException("Error while resetting " + repoKey);
        }
    }

    private Header getAuthorizationHeader() {
        String auth = DEFAULT_USERNAME + ":" + UPDATED_PASSWORD;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(UTF_8));
        String authHeader = "Basic " + new String(encodedAuth, UTF_8);
        return new BasicHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }

    public void stop() {
        artifactoryContainer.stop();
    }

    private URI toUri(String url) {
        try {
            return new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Error while creating the URI", e);
        }
    }

    public String getReleaseRepoUrl() {
        return format("%s/%s", getBaseUiUrl(), RELEASE_REPO_KEY);
    }

    public String getInternalReleaseRepoUrl() {
        return format("%s/%s", getBaseApiInternalUrl(), RELEASE_REPO_KEY);
    }

    public String getSnapshotRepoUrl() {
        return format("%s/%s", getBaseUiUrl(), SNAPSHOT_REPO_KEY);
    }

    public String getInternalSnapshotRepoUrl() {
        return format("%s/%s", getBaseApiInternalUrl(), SNAPSHOT_REPO_KEY);
    }

    private String getBaseApiUrl() {
        return format("http://%s:%s/artifactory", artifactoryContainer.getHost(), artifactoryContainer.getMappedPort(8081));
    }

    public String getBaseApiInternalUrl() {
        return format("http://%s:%s/artifactory", artifactoryContainer.getNetworkAliases().get(0), 8081);
    }

    private String getBaseUiUrl() {
        return format("http://%s:%s/artifactory", artifactoryContainer.getHost(), artifactoryContainer.getMappedPort(8082));
    }

    public String getBaseUiInternalUrl() {
        return format("http://%s:%s/artifactory", artifactoryContainer.getNetworkAliases().get(0), 8082);
    }

    private void waitUntilArtifactoryIsInitialized() {
        Awaitility.await()
            .atMost(STARTUP_TIMEOUT)
            .pollInterval(Duration.ofSeconds(3))
            .until(() -> {
                boolean contains = artifactoryContainer.getLogs().contains("All services started successfully in");
                log.info("Waiting for Artifactory to start up..");
                return contains;
            });
    }

    @Data
    private static class ListResponse {
        private List<ListResponseChildren> children;
    }

    @Data
    private static class ListResponseChildren {
        private String uri;
    }
}
