package com.docktape.swagger.brake.core.model;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Request {
    private final Map<MediaType, Schema> mediaTypes;

    public Optional<Schema> getSchemaByMediaType(MediaType mediaType) {
        return Optional.ofNullable(mediaTypes.get(mediaType));
    }

    /**
     * Checks if a specific media type is allowed by this {@link Request} instance. If the {@link MediaType#ALL} is
     * present within the {@link Request}, it automatically considers the given media type as allowed.
     * @param mediaType the {@link MediaType}
     * @return true if the {@link MediaType} is present or if amongst the media types, {@link MediaType#ALL} is present.
     *      false otherwise.
     */
    public boolean isMediaTypeAllowed(MediaType mediaType) {
        Set<MediaType> availableMediaTypes = mediaTypes.keySet();
        if (availableMediaTypes.contains(MediaType.ALL)) {
            return true;
        }
        return availableMediaTypes.contains(mediaType);
    }
}
