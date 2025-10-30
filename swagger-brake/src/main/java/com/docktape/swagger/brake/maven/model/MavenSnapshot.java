package com.docktape.swagger.brake.maven.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JacksonXmlRootElement(localName = "snapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MavenSnapshot {
    private String timestamp;
    private String buildNumber;
}
