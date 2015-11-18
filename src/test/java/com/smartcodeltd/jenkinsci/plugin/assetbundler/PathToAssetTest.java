package com.smartcodeltd.jenkinsci.plugin.assetbundler;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.smartcodeltd.jenkinsci.plugin.assetbundler.OsSpecific.isNotWindows;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

public class PathToAssetTest {
    @Before
    public void checkOS() {
        // todo: Build Monitor should use Java 7 once its core dependency on Jenkins can be upgraded to 1.612
        assumeTrue(isNotWindows());
    }

    @Test
    public void combines_base_resource_url_given_by_jenkins_and_a_path_to_the_asset() throws Exception {
        URL base = baseResourceUrlGivenByJenkins("/opt/jenkins/plugins/build-monitor-plugin/");

        PathToAsset path = new PathToAsset(base, "less/index.less");

        assertThat(absolute(path), is("/opt/jenkins/plugins/build-monitor-plugin/less/index.less"));
    }

    private String absolute(PathToAsset path) {
        return path.toFile().getAbsolutePath();
    }

    private URL baseResourceUrlGivenByJenkins(String path) throws MalformedURLException {
        return new URL("file://" + path);
    }
}