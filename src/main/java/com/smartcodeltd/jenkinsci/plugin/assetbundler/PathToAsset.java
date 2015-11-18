package com.smartcodeltd.jenkinsci.plugin.assetbundler;

import java.io.File;
import java.net.URI;
import java.net.URL;

import static java.lang.String.format;

public class PathToAsset {
    private static final String FILE_PROTOCOL = "file";
    private final URI path;

    public PathToAsset(URL root, String asset) {
        this.path = validated(uriFrom(root, asset));
    }

    public File toFile() {
        /*
         * The below workaround is required as the `uri` representing a UNC path on Windows
         * has an `authority` component defined (//foo/bar/some/file.ext),
         * however the File class constructor requires URIs with no defined authority
         *
         * https://github.com/jan-molak/jenkins-build-monitor-plugin/issues/183#issuecomment-157712010
         *
         * http://blogs.msdn.com/b/ie/archive/2006/12/06/file-uris-in-windows.aspx
         * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5086147
         *
         * fix as per: https://github.com/sbt/sbt/issues/564
         */

        return (null == path.getAuthority())
                ? new File(path)
                : new File(path.getSchemeSpecificPart());
    }

    // --

    private URI uriFrom(URL root, String asset) {
        try {
            return new URL(root, asset).toURI();
        } catch (Exception e) {
            throw new IllegalArgumentException(format("Sorry, I couldn't construct a path to asset '%s' using root: '%s", asset, root));
        }
    }

    private URI validated(URI fileUri) {
        if (! FILE_PROTOCOL.equals(fileUri.getScheme())) {
            throw new IllegalArgumentException(format("Sorry, can't load the asset from '%s' using the '%s' protocol", fileUri, FILE_PROTOCOL));
        }

        return fileUri;
    }
}
