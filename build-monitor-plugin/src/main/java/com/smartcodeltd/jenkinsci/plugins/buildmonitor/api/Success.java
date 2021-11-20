package com.smartcodeltd.jenkinsci.plugins.buildmonitor.api;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/* package */ class Success<T> {

    private final long startTimeNanos;
    private T data;

    public Success(T data) {
        this.data = data;
        this.startTimeNanos = System.nanoTime();
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
        return Collections.singletonMap(
                "response_time_ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTimeNanos, TimeUnit.NANOSECONDS)
        );
    }
}
