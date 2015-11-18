package com.smartcodeltd.jenkinsci.plugin.assetbundler;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static java.lang.String.format;

public class PathToAsset {
    private final URL root;
    private final String asset;

    public PathToAsset(URL root, String asset) {
        this.root = root;
        this.asset = asset;
    }

    public URI toURI() {
        try {
            return new URL(root, asset).toURI();
        } catch (Exception e) {
            throw new RuntimeException(format("Sorry, I couldn't construct a path to asset '%s' using root: '%s", asset, root));
        }
    }

    public File toFile() {
        return new File(toURI());
    }
}
