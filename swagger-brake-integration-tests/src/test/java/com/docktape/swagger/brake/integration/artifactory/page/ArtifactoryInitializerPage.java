package com.docktape.swagger.brake.integration.artifactory.page;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ArtifactoryInitializerPage extends PageBase {
    @FindBy(className = "get-started")
    private WebElement getStartedButton;

    @FindBy(css = "input[type='password']")
    private List<WebElement> passwordInputs;

    @FindBy(css = "#onboarding-main-wrapper button.el-button.el-button--primary")
    private WebElement nextButton;

    @FindBy(css = "#onboarding-main-wrapper button.el-button.el-button--secondary")
    private List<WebElement> otherButtons;

    public void waitForPageLoad() {
        waitUntilVisible(getStartedButton, Duration.ofSeconds(5));
    }

    public void clickGetStarted() {
        click(getStartedButton);
    }

    public void enterNewPassword(String newPassword) {
        type(passwordInputs.get(0), newPassword);
        type(passwordInputs.get(1), newPassword);
    }

    public void next() {
        click(nextButton);
    }

    public void finish() {
        // it has the same selector
        click(nextButton);
    }

    public void skip() {
        click(getSkipButton());
    }

    private WebElement getSkipButton() {
        return otherButtons.get(1);
    }
}
