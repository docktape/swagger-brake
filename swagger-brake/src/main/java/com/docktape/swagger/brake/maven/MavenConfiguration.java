package com.docktape.swagger.brake.maven;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.docktape.swagger.brake.maven.http.HttpClientErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class MavenConfiguration {
    @Bean
    public HttpClient httpClient() {
        return HttpClientBuilder.create().addInterceptorLast(new HttpClientErrorHandler()).build();
    }

    @Bean
    public XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return xmlMapper;
    }
}
