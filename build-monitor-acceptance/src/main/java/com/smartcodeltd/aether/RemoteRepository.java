package com.smartcodeltd.aether;

import java.net.URI;

public class RemoteRepository {

    private final String id;
    private final String type;
    private final String url;

    public RemoteRepository(String id, String type, String url) {
        this.id = id;
        this.type = type;
        this.url = url;
    }

    public static RemoteRepository at(String url) {
        URI remote = URI.create(url);

        return new RemoteRepository(remote.getHost(), "default", remote.toString());
    }

    public String id() {
        return id;
    }

    public String type() {
        return type;
    }

    public String url() {
        return url;
    }
}
