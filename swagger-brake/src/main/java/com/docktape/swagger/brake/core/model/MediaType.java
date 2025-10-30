package com.docktape.swagger.brake.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Getter
@EqualsAndHashCode
@ToString
public class MediaType {
    public static final MediaType ALL = new MediaType("*/*");

    private final MimeType mediaType;
    private final String mimeType;

    public MediaType(String mimeType) {
        this.mimeType = mimeType;
        this.mediaType = MimeTypeUtils.parseMimeType(mimeType);
    }
}
