package com.smartcodeltd.jenkinsci.plugins.buildmonitor.api;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/* package */ class Success<T> {

    private final Stopwatch stopwatch;
    private T data;

    public Success(T data) {
        this.data = data;
        this.stopwatch = new Stopwatch();
        stopwatch.start();
    }

    public static <T> Success successful(T data) {
        return new Success<T>(data);
    }

    @JsonProperty
    public T data() {
        return data;
    }

    @JsonProperty
    public Map<String, ?> meta() {
        return ImmutableMap.<String, Object>of(
                "response_time_ms", stopwatch.elapsedMillis()
        );
    }
}
