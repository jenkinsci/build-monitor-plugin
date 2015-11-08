package com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade;

public class BuildMonitorInstallation {
    private static final BuildMonitorBuildProperties buildProperties = new BuildMonitorBuildProperties("build-monitor.properties");

    public String buildMonitorVersion() {
        return buildProperties.get("version");
    }
}