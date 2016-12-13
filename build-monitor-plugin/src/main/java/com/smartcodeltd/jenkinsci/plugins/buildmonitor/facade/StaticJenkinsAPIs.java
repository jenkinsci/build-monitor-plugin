package com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade;

import hudson.Main;
import hudson.model.BuildBadgeAction;
import hudson.model.PageDecorator;
import hudson.model.User;
import jenkins.model.Jenkins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.jenkinsci.main.modules.instance_identity.PageDecoratorImpl;
import org.kohsuke.stapler.MetaClass;
import org.kohsuke.stapler.WebApp;
import org.kohsuke.stapler.jelly.JellyClassLoaderTearOff;

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

    public URL getBadgeJelly(BuildBadgeAction badge) {
		String jelly = badge.getClass().getSimpleName() + "/badge.jelly";
		return badge.getClass().getResource(jelly);
	}

    private JellyContext createJellyScript(Object it) {
    	MetaClass mc = WebApp.getCurrent().getMetaClass(getClass());
    	JellyContext jellyContext = mc.classLoader.loadTearOff(JellyClassLoaderTearOff.class).createContext();

    	jellyContext.setVariable("app", Jenkins.getInstance());
    	jellyContext.setVariable("rootURL", Jenkins.getInstance().getRootUrl());
    	jellyContext.setVariable("h", new hudson.Functions());
    	jellyContext.setVariable("resURL", Jenkins.RESOURCE_PATH);
    	jellyContext.setVariable("imagesURL", Jenkins.RESOURCE_PATH + "/images");
    	jellyContext.setVariable("it", it);

		return jellyContext;
    }

    public byte[] runJellyScript(URL jellyFile, Object it) throws IOException, JellyException {
    	JellyContext jellyContext = createJellyScript(it);

		ByteArrayOutputStream html = new ByteArrayOutputStream();
		XMLOutput xmlOutput = XMLOutput.createXMLOutput(html);
		jellyContext.runScript(jellyFile, xmlOutput);
		xmlOutput.flush();

		return html.toByteArray();
    }
}
