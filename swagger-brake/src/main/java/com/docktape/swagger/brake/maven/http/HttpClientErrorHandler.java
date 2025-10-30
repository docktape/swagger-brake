package com.docktape.swagger.brake.maven.http;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpClientErrorHandler implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (HttpStatus.SC_UNAUTHORIZED == statusCode) {
            if (log.isDebugEnabled()) {
                String body = EntityUtils.toString(response.getEntity());
                log.debug("Unauthorized access with response\n{}", body);
            }
            throw new UnauthorizedException("Request unauthorized");
        }
    }
}
