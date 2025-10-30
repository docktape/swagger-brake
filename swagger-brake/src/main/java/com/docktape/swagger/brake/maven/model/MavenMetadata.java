package com.docktape.swagger.brake.maven.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenMetadata {
    private String groupId;
    private String artifactId;
    private MavenVersioning versioning;
}
