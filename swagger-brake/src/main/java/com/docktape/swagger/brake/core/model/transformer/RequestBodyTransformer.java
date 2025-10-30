package com.docktape.swagger.brake.core.model.transformer;

import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Set;

import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.Request;
import com.docktape.swagger.brake.core.model.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestBodyTransformer implements Transformer<RequestBody, Request> {
    private final MediaTypeTransformer mediaTypeTransformer;

    @Override
    public Request transform(RequestBody from) {
        Set<Map.Entry<String, io.swagger.v3.oas.models.media.MediaType>> entries = from.getContent().entrySet();
        Map<MediaType, Schema> mediaTypes = entries.stream().collect(toMap(e -> new MediaType(e.getKey()), e -> mediaTypeTransformer.transform(e.getValue())));
        return new Request(mediaTypes);
    }
}
