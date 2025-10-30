package com.docktape.swagger.brake.integration.artifactory.page;

import java.time.Duration;

import com.docktape.swagger.brake.integration.artifactory.factory.PageFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public abstract class PageBase {
    private WebDriver webDriver;

    protected PageBase() {
    }

    protected void waitUntilVisible(WebElement webElement) {
        waitUntilVisible(webElement, Duration.ofSeconds(5));
    }

    protected void waitUntilVisible(WebElement webElement, Duration duration) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, duration);
            wait.until(ExpectedConditions.visibilityOf(webElement));
        } catch (TimeoutException e) {
            throw new RuntimeException("Element is not visible", e);
        }
    }

    protected void type(WebElement webElement, String text) {
        webElement.sendKeys(text);
    }

    protected void click(WebElement webElement) {
        waitUntilClickable(webElement);
        try {
            webElement.click();
        } catch (ElementClickInterceptedException e) {
            log.info("Element click intercepted, let's try again after waiting a little bit");
            waitFor(2);
            webElement.click();
        }
    }

    protected boolean isCheckboxChecked(WebElement checkbox) {
        return checkbox.isSelected();
    }

    private void waitUntilClickable(WebElement webElement) {
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (TimeoutException e) {
            throw new RuntimeException("Element is not clickable", e);
        }
    }

    protected <R extends PageBase> R proceed(Class<R> newPageClazz) {
        return new PageFactory(webDriver).create(newPageClazz);
    }

    protected void scrollIntoView(WebElement webElement) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

    public void waitFor(long seconds) {
        waitFor(Duration.ofSeconds(seconds));
    }

    public void waitFor(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
