package com.docktape.swagger.brake.core.rule.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.docktape.swagger.brake.core.model.HttpMethod;
import com.docktape.swagger.brake.core.model.MediaType;
import com.docktape.swagger.brake.core.model.Path;
import com.docktape.swagger.brake.core.model.Response;
import com.docktape.swagger.brake.core.model.Schema;
import com.docktape.swagger.brake.core.model.Specification;
import com.docktape.swagger.brake.core.rule.PathSkipper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponseMediaTypeGeneralizedRuleTest {
    private PathSkipper pathSkipper;
    private ResponseMediaTypeGeneralizedRule underTest;

    @BeforeEach
    void setUp() {
        pathSkipper = mock(PathSkipper.class);
        when(pathSkipper.shouldSkip(any())).thenReturn(false);
        underTest = new ResponseMediaTypeGeneralizedRule(pathSkipper);
    }

    @Test
    void testVendorJsonGeneralizedToApplicationJsonIsDetected() {
        // given
        MediaType oldMediaType = new MediaType("application/vnd.api+json");
        MediaType newMediaType = new MediaType("application/json");
        Schema schema = mock(Schema.class);

        Map<MediaType, Schema> oldMediaTypes = new HashMap<>();
        oldMediaTypes.put(oldMediaType, schema);
        Response oldResponse = new Response("200", oldMediaTypes);

        Map<MediaType, Schema> newMediaTypes = new HashMap<>();
        newMediaTypes.put(newMediaType, schema);
        Response newResponse = new Response("200", newMediaTypes);

        Path oldPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(oldResponse), false, false);
        Path newPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(newResponse), false, false);

        Specification oldApi = new Specification(List.of(oldPath));
        Specification newApi = new Specification(List.of(newPath));

        // when
        Collection<ResponseMediaTypeGeneralizedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).hasSize(1);
        ResponseMediaTypeGeneralizedBreakingChange bc = result.iterator().next();
        assertThat(bc.getOldMediaType()).isEqualTo("application/vnd.api+json");
        assertThat(bc.getNewMediaType()).isEqualTo("application/json");
        assertThat(bc.getResponseCode()).isEqualTo("200");
    }

    @Test
    void testSameMediaTypeIsNotFlagged() {
        // given
        MediaType mediaType = new MediaType("application/json");
        Schema schema = mock(Schema.class);

        Map<MediaType, Schema> mediaTypes = new HashMap<>();
        mediaTypes.put(mediaType, schema);
        Response response = new Response("200", mediaTypes);

        Path oldPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(response), false, false);
        Path newPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(response), false, false);

        Specification oldApi = new Specification(List.of(oldPath));
        Specification newApi = new Specification(List.of(newPath));

        // when
        Collection<ResponseMediaTypeGeneralizedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testBothOldAndNewTypePresentIsNotFlagged() {
        // given
        MediaType oldMediaType = new MediaType("application/vnd.api+json");
        MediaType newGeneralMediaType = new MediaType("application/json");
        Schema schema = mock(Schema.class);

        Map<MediaType, Schema> oldMediaTypes = new HashMap<>();
        oldMediaTypes.put(oldMediaType, schema);
        Response oldResponse = new Response("200", oldMediaTypes);

        Map<MediaType, Schema> newMediaTypes = new HashMap<>();
        newMediaTypes.put(oldMediaType, schema);
        newMediaTypes.put(newGeneralMediaType, schema);
        Response newResponse = new Response("200", newMediaTypes);

        Path oldPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(oldResponse), false, false);
        Path newPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(newResponse), false, false);

        Specification oldApi = new Specification(List.of(oldPath));
        Specification newApi = new Specification(List.of(newPath));

        // when
        Collection<ResponseMediaTypeGeneralizedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void testVendorXmlGeneralizedToApplicationXmlIsDetected() {
        // given
        MediaType oldMediaType = new MediaType("application/vnd.api+xml");
        MediaType newMediaType = new MediaType("application/xml");
        Schema schema = mock(Schema.class);

        Map<MediaType, Schema> oldMediaTypes = new HashMap<>();
        oldMediaTypes.put(oldMediaType, schema);
        Response oldResponse = new Response("200", oldMediaTypes);

        Map<MediaType, Schema> newMediaTypes = new HashMap<>();
        newMediaTypes.put(newMediaType, schema);
        Response newResponse = new Response("200", newMediaTypes);

        Path oldPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(oldResponse), false, false);
        Path newPath = new Path("/pets", HttpMethod.GET, null, Collections.emptyList(), List.of(newResponse), false, false);

        Specification oldApi = new Specification(List.of(oldPath));
        Specification newApi = new Specification(List.of(newPath));

        // when
        Collection<ResponseMediaTypeGeneralizedBreakingChange> result = underTest.checkRule(oldApi, newApi);

        // then
        assertThat(result).hasSize(1);
        ResponseMediaTypeGeneralizedBreakingChange bc = result.iterator().next();
        assertThat(bc.getOldMediaType()).isEqualTo("application/vnd.api+xml");
        assertThat(bc.getNewMediaType()).isEqualTo("application/xml");
    }
}
