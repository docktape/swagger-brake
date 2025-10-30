package com.docktape.swagger.brake.integration.project.plugin;

import com.docktape.swagger.brake.integration.project.ProjectParameter;
import com.docktape.swagger.brake.integration.support.SwaggerBrakeVersion;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PluginProjectParameter extends ProjectParameter {
    private SwaggerBrakeVersion swaggerBrakeVersion;
    private boolean removePackaging = false;
    private String artifactId;
}
