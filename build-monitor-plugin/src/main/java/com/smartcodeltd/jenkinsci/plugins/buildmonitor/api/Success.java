package com.smartcodeltd.jenkinsci.plugins.buildmonitor.api;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        return new Success<>(data);
    }

    @JsonProperty
    public T data() {
        return data;
    }

    @JsonProperty
    public Map<String, ?> meta() {
        return Map.of(
                "response_time_ms",
                TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTimeNanos, TimeUnit.NANOSECONDS));
    }
}
