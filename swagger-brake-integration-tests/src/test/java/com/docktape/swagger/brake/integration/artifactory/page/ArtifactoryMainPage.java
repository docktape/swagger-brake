package com.docktape.swagger.brake.integration.artifactory.page;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ArtifactoryMainPage extends PageBase {
    @FindBy(css = ".icons img[alt='admin']")
    private WebElement adminIcon;

    @FindBy(css = ".vsm--link.vsm--link_level-1")
    private List<WebElement> settingsButtons;

    @FindBy(css = "a.vsm--link.vsm--link_level-2")
    private List<WebElement> subRepositoriesButtons;

    public void openAdminPanel() {
        click(adminIcon);
    }

    public ArtifactoryRepositoriesPage openRepositoriesSettings() {
        click(getRepositoriesButton());
        click(getSubRepositoriesButton());
        return proceed(ArtifactoryRepositoriesPage.class);
    }

    private WebElement getRepositoriesButton() {
        return settingsButtons.get(0);
    }

    private WebElement getSubRepositoriesButton() {
        return subRepositoriesButtons.get(0);
    }
}
