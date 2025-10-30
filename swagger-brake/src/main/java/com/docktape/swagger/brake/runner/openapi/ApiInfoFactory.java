package com.docktape.swagger.brake.runner.openapi;

import com.docktape.swagger.brake.core.ApiInfo;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.stereotype.Component;

@Component
public class ApiInfoFactory {
    public ApiInfo create(OpenAPI openApi) {
        ApiInfo apiInfo = new ApiInfo();
        Info info = openApi.getInfo();
        if (info != null) {
            apiInfo.setTitle(info.getTitle());
            apiInfo.setDescription(info.getDescription());
            apiInfo.setVersion(info.getVersion());
        }
        return apiInfo;
    }
}
