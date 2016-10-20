package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.collect.ImmutableList;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

import hudson.model.BuildBadgeAction;
import jenkins.model.Jenkins;

import static com.google.common.collect.Lists.newArrayList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.codehaus.jackson.annotate.JsonValue;
import org.kohsuke.stapler.MetaClass;
import org.kohsuke.stapler.WebApp;
import org.kohsuke.stapler.jelly.JellyClassLoaderTearOff;

/**
 * @author Jan Molak
 */
public class BadgesDetails implements Feature<BadgesDetails.Badges> {
    private JobView job;

    public BadgesDetails() {

    }

    @Override
    public BadgesDetails of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Badges asJson() {
        return !job.lastCompletedBuild().badgeActions().isEmpty()                  // would be nice to have .map(Claim(_)).orElse(), but hey...
                ? new Badges(job.lastCompletedBuild().badgeActions())
                : null;                             // `null` because we don't want to serialise an empty object
    }

    public static class Badges {

        private final List<String> badges = newArrayList();

        public Badges(List<BuildBadgeAction> badgeActions) {
        	MetaClass mc = WebApp.getCurrent().getMetaClass(getClass());
    		JellyContext context = mc.classLoader.loadTearOff(JellyClassLoaderTearOff.class).createContext();

    		context.setVariable("app", Jenkins.getInstance());
    		context.setVariable("rootURL", Jenkins.getInstance().getRootUrl());
    		context.setVariable("h", new hudson.Functions());
    		context.setVariable("resURL", Jenkins.RESOURCE_PATH);
    		context.setVariable("imagesURL", Jenkins.RESOURCE_PATH + "/images");

    		for( BuildBadgeAction badge : badgeActions ) {
    			context.setVariable("it", badge);

    			String jelly = badge.getClass().getSimpleName() + "/badge.jelly";
    			URL jellyFile = badge.getClass().getResource(jelly);

    			if( jellyFile != null ) {
    				try {
    					ByteArrayOutputStream html = new ByteArrayOutputStream();
    					XMLOutput xmlOutput = XMLOutput.createXMLOutput(html);
    					context.runScript(jellyFile, xmlOutput);
    					xmlOutput.flush();

    					badges.add(new String(html.toByteArray()));
    				} catch (JellyException ex) {
    					// To be logged
    				} catch (IOException ex) {
    					// To be logged
    				}
    			}
    		}
       }

        @JsonValue
        public List<String> value() {
            return ImmutableList.copyOf(badges);
        }
    }

}
