package com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade;

import hudson.Main;
import hudson.model.PageDecorator;
import hudson.model.User;
import jenkins.model.Jenkins;
import org.jenkinsci.main.modules.instance_identity.PageDecoratorImpl;

public class StaticJenkinsAPIs {
    public boolean isDevelopmentMode() {
        return Main.isUnitTest || Main.isDevelopmentMode;
    }

    public String encodedPublicKey() {
        return Jenkins.getInstance().getExtensionList(PageDecorator.class).get(PageDecoratorImpl.class).getEncodedPublicKey();
    }

    public int numberOfUsers() {
        return User.getAll().size();
    }

    // see https://github.com/jan-molak/jenkins-build-monitor-plugin/issues/215
    public String crumbFieldName() {
        Jenkins instance = Jenkins.getInstance();

        return instance.isUseCrumbs() ? instance.getCrumbIssuer().getCrumbRequestField() : ".crumb";
    }

    public boolean hasPlugin(String pluginName) {
        return Jenkins.getInstance().getPlugin(pluginName) != null;
    }
}
