package com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade;

import jenkins.model.Jenkins;

public class StaticJenkinsAPIs {

    public boolean hasPlugin(String pluginName) {
        return Jenkins.get().getPlugin(pluginName) != null;
    }
}
