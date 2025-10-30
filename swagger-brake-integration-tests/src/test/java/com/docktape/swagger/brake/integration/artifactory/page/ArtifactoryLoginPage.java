package com.docktape.swagger.brake.integration.artifactory.page;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ArtifactoryLoginPage extends PageBase {
    @FindBy(className = "login-submit-button")
    private WebElement loginSubmitButton;

    @FindBy(css = "input[name='username']")
    private WebElement usernameInput;

    @FindBy(css = "input[name='password']")
    private WebElement passwordInput;

    public void waitForPageLoad() {
        waitUntilVisible(loginSubmitButton, Duration.ofSeconds(30));
    }

    public void enterCredentials(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
    }

    public void submitCredentials() {
        click(loginSubmitButton);
    }
}
