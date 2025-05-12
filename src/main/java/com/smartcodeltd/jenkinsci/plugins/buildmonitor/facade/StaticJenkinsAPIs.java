package com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Main;
import hudson.PluginWrapper;
import hudson.model.PageDecorator;
import hudson.model.User;
import hudson.security.csrf.CrumbIssuer;
import jenkins.model.Jenkins;
import org.jenkinsci.main.modules.instance_identity.PageDecoratorImpl;

public class StaticJenkinsAPIs {
    public boolean isDevelopmentMode() {
        return Main.isUnitTest || Main.isDevelopmentMode;
    }

    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "PageDecoratorImpl is part of Jenkins core and should never be null")
    public String encodedPublicKey() {
        return Jenkins.get()
                .getExtensionList(PageDecorator.class)
                .get(PageDecoratorImpl.class)
                .getEncodedPublicKey();
    }

    public int numberOfUsers() {
        return User.getAll().size();
    }

    // see https://github.com/jenkinsci/build-monitor-plugin/issues/215
    public String crumbFieldName() {
        Jenkins instance = Jenkins.get();

        if (instance.isUseCrumbs()) {
            CrumbIssuer crumbIssuer = instance.getCrumbIssuer();
            if (crumbIssuer == null) {
                throw new RuntimeException("No CrumbIssuer found");
            }
            return crumbIssuer.getCrumbRequestField();
        }
        return ".crumb";
    }

    public boolean hasPlugin(String pluginName) {
        return Jenkins.get().getPlugin(pluginName) != null;
    }

    public String getPluginVersion(String pluginName) {
        PluginWrapper plugin = Jenkins.get().getPluginManager().getPlugin(pluginName);
        return plugin != null ? plugin.getVersion() : null;
    }
}
