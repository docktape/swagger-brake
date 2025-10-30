package com.docktape.swagger.brake.integration.artifactory.page;

import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class ArtifactoryNewRepositoryPage extends PageBase {
    @FindBy(className = "iconrepo-maven")
    private WebElement mavenRepositoryButton;

    @FindBy(id = "repoKey-new")
    private WebElement repoKeyInput;

    @FindBy(id = "handleReleases")
    private WebElement handleReleasesCheckbox;

    @FindBy(xpath = "//input[@id='handleReleases']/following-sibling::span")
    private WebElement handleReleasesCheckboxButton;

    @FindBy(id = "handleSnapshots")
    private WebElement handleSnapshotsCheckbox;

    @FindBy(xpath = "//input[@id='handleSnapshots']/following-sibling::span")
    private WebElement handleSnapshotsCheckboxButton;

    @FindBy(id = "repository-save-button")
    private WebElement saveAndFinishButton;

    public ArtifactoryNewRepositoryPage selectMavenRepository() {
        click(mavenRepositoryButton);
        return this;
    }

    public ArtifactoryNewRepositoryPage enterRepoKey(String repoKey) {
        type(repoKeyInput, repoKey);
        return this;
    }

    public ArtifactoryNewRepositoryPage uncheckHandleReleases() {
        scrollIntoView(handleReleasesCheckboxButton);
        waitUntilVisible(handleReleasesCheckboxButton);
        boolean checkboxChecked = isCheckboxChecked(handleReleasesCheckbox);
        log.info("checkboxChecked {}", checkboxChecked);
        if (checkboxChecked) {
            log.info("clicking button");
            click(handleReleasesCheckboxButton);
            log.info("button clicked");
        }
        Awaitility.await().pollInterval(Duration.ofMillis(200)).until(() -> !isCheckboxChecked(handleReleasesCheckbox));
        return this;
    }

    public ArtifactoryNewRepositoryPage uncheckHandleSnapshots() {
        scrollIntoView(handleSnapshotsCheckboxButton);
        waitUntilVisible(handleSnapshotsCheckboxButton);
        boolean checkboxChecked = isCheckboxChecked(handleSnapshotsCheckbox);
        log.info("checkboxChecked {}", checkboxChecked);
        if (checkboxChecked) {
            log.info("clicking button");
            click(handleSnapshotsCheckboxButton);
            log.info("button clicked");
        }
        Awaitility.await().pollInterval(Duration.ofMillis(200)).until(() -> !isCheckboxChecked(handleSnapshotsCheckbox));
        return this;
    }

    public void saveAndFinish() {
        click(saveAndFinishButton);
    }
}
