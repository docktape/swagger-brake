package com.docktape.swagger.brake.integration.artifactory.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ArtifactoryRepositoriesPage extends PageBase {
    @FindBy(className = "new-entity")
    private WebElement newLocalRepositoryButton;

    public ArtifactoryNewRepositoryPage newLocalRepository() {
        click(newLocalRepositoryButton);
        return proceed(ArtifactoryNewRepositoryPage.class);
    }
}
