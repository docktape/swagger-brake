package com.docktape.swagger.brake.report.html;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Component;

@Component
public class MustacheContentResolver {
    /**
     * Resolves a mustache template into a String based on the parameter map.
     * @param mustacheFile the path of the mustache template.
     * @param paramMap the parameter map for the mustache template.
     * @return the resolved content.
     */
    public String resolve(String mustacheFile, Map<String, ?> paramMap) {
        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache m = mf.compile(mustacheFile);
            StringWriter sw = new StringWriter();
            m.execute(sw, paramMap).flush();
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while resolving mustache content", e);
        }
    }
}
