package com.docktape.swagger.brake.integration.artifactory.factory;

import java.lang.reflect.Constructor;

import com.docktape.swagger.brake.integration.artifactory.page.PageBase;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.util.ReflectionUtils;

@RequiredArgsConstructor
public class PageFactory {
    private final WebDriver webDriver;

    public <T extends PageBase> T create(Class<T> pageClazz) {
        try {
            Constructor<T> emptyConstructor = pageClazz.getDeclaredConstructor();
            emptyConstructor.setAccessible(true);
            T pageObject = emptyConstructor.newInstance();
            setField(pageObject, webDriver);
            org.openqa.selenium.support.PageFactory.initElements(webDriver, pageObject);
            return pageObject;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create page object", e);
        }
    }

    private <T extends PageBase, R> void setField(T pageObject, R fieldInstance) {
        ReflectionUtils.doWithFields(pageObject.getClass(), field -> {
            if (field.getType().isAssignableFrom(fieldInstance.getClass())) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, pageObject, fieldInstance);
            }
        });
    }
}
