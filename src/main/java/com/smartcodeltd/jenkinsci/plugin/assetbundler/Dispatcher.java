package com.smartcodeltd.jenkinsci.plugin.assetbundler;

import com.smartcodeltd.jenkinsci.plugin.assetbundler.filters.LessCSS;
import hudson.Extension;
import hudson.Plugin;
import hudson.util.PluginServletFilter;

import java.net.URISyntaxException;

/*
 *   Based on:
 *   * https://github.com/jenkinsci/uithemes-plugin/blob/master/src/main/java/org/jenkinsci/plugins/uithemes/
 *   * https://github.com/kiy0taka/filter_sample/
 *
 *   and the conversation at
 *   * https://groups.google.com/forum/#!searchin/jenkinsci-dev/less/jenkinsci-dev/3AU7OcUxRFk/YNpFl2NS47QJ
 */
@Extension
public class Dispatcher extends Plugin {

    @Override
    public void postInitialize() throws Exception {
        super.postInitialize();

        PluginServletFilter.addFilter(new LessCSS("/build-monitor-plugin/style.css", pathToIndexLess()));
    }

    private String pathToIndexLess() throws URISyntaxException {
        String base = getWrapper().parent.getPlugin("build-monitor-plugin").baseResourceURL.toURI().toString();

        return (base + "/less/index.less").replaceAll("/+", "/");
    }
}
