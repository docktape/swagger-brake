package com.docktape.swagger.brake.maven.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "versioning")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenVersioning {
    private String latest;
    private String release;
    private MavenSnapshot snapshot;
}
