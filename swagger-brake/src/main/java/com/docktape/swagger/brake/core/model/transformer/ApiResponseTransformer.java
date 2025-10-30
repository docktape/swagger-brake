package com.docktape.swagger.brake.core.model.transformer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.service.TypeRefNameResolver;
import com.docktape.swagger.brake.core.model.store.ResponseStore;
import com.docktape.swagger.brake.core.model.store.StoreProvider;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiResponseTransformer implements Transformer<Pair<String, ApiResponse>, Response> {
    private final MediaTypeTransformer mediaTypeTransformer;
    private final TypeRefNameResolver typeRefNameResolver;

    @Override
    public Response transform(Pair<String, ApiResponse> from) {
        Map<MediaType, Schema> schemaRefs = new HashMap<>();
        Content content = from.getValue().getContent();
        if (content != null) {
            Set<Map.Entry<String, io.swagger.v3.oas.models.media.MediaType>> entries = resolveReferencedSchemas(content.entrySet());
            for (Map.Entry<String, io.swagger.v3.oas.models.media.MediaType> entry : entries) {
                MediaType key = new MediaType(entry.getKey());
                Schema value = mediaTypeTransformer.transform(entry.getValue());
                if (value != null) {
                    schemaRefs.put(key, value);
                }
            }
        }
        return new Response(from.getKey(), schemaRefs);
    }

    /*
    Needed to handle the case when the schema reference is not from the schemas section but from the responses section.
    Apparently according to the specification its not allowed but even the official Swagger validator does not fail on this, so support is required.
     */
    private Set<Map.Entry<String, io.swagger.v3.oas.models.media.MediaType>> resolveReferencedSchemas(Set<Map.Entry<String, io.swagger.v3.oas.models.media.MediaType>> entries) {
        Map<String, io.swagger.v3.oas.models.media.MediaType> result = new HashMap<>();
        for (Map.Entry<String, io.swagger.v3.oas.models.media.MediaType> entry : entries) {
            io.swagger.v3.oas.models.media.MediaType mediaType = entry.getValue();
            String schemaRef = Optional.ofNullable(mediaType.getSchema()).map(io.swagger.v3.oas.models.media.Schema::get$ref).orElse(null);
            if (schemaRef == null) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                if (isSchemaFromResponsesSection(schemaRef)) {
                    io.swagger.v3.oas.models.media.MediaType replacedMediaType = copyMediaType(mediaType);
                    io.swagger.v3.oas.models.media.Schema resolvedSchema = getResolvedSchema(schemaRef, entry.getKey());
                    replacedMediaType.setSchema(resolvedSchema);
                    result.put(entry.getKey(), replacedMediaType);
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result.entrySet();
    }

    private io.swagger.v3.oas.models.media.Schema getResolvedSchema(String originalRefName, String mimeType) {
        if (originalRefName == null || mimeType == null) {
            return null;
        }
        ResponseStore responseStore = StoreProvider.provideResponseStore();
        if (responseStore == null) {
            return null;
        }

        String refName = typeRefNameResolver.resolve(originalRefName);
        return responseStore.get(refName)
            .map(r -> {
                Content content = r.getContent();
                io.swagger.v3.oas.models.media.MediaType mediaType = content.get(mimeType);
                if (mediaType == null) {
                    mediaType = content.get(MediaType.ALL.getMimeType());
                }
                if (mediaType == null) {
                    throw new IllegalStateException("No mediatype definition found");
                }
                return mediaType.getSchema();
            })
            .orElseThrow(() -> new IllegalStateException("Reference not found for " + refName));
    }

    private boolean isSchemaFromResponsesSection(String schemaRef) {
        return schemaRef.contains("/responses/");
    }

    private io.swagger.v3.oas.models.media.MediaType copyMediaType(io.swagger.v3.oas.models.media.MediaType mediaType) {
        io.swagger.v3.oas.models.media.MediaType result = new io.swagger.v3.oas.models.media.MediaType();
        result.setEncoding(mediaType.getEncoding());
        result.setExample(mediaType.getExample());
        result.setExamples(mediaType.getExamples());
        result.setExtensions(mediaType.getExtensions());
        result.setSchema(mediaType.getSchema());
        return result;
    }
}
