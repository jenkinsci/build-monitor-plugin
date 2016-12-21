package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import com.google.common.base.Supplier;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;

import hudson.model.BuildBadgeAction;

import java.net.URL;

import org.apache.commons.jelly.JellyException;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Jan Molak
 */
public class BadgeRecipe implements Supplier<BuildBadgeAction> {
    private BuildBadgeAction badge;
    private StaticJenkinsAPIs jenkins;

    public BadgeRecipe(StaticJenkinsAPIs jenkins) {
    	badge = mock(BuildBadgeAction.class);
    	this.jenkins = jenkins;
    }

    public BadgeRecipe withJelly() throws Exception {
    	URL jellyFile = PowerMockito.mock(URL.class);
    	when(jenkins.getBadgeJelly(badge)).thenReturn(jellyFile);
    	
    	String html = "<span>text</span>";
    	when(jenkins.runJellyScript(jellyFile, badge)).thenReturn(html.getBytes());
    	
        return this;
    }

    public BadgeRecipe withoutJelly() throws Exception {
    	when(jenkins.getBadgeJelly(badge)).thenReturn(null);
    	
        return this;
    }

    public BadgeRecipe withInvalidJelly() throws Exception {
    	URL jellyFile = PowerMockito.mock(URL.class);
    	when(jenkins.getBadgeJelly(badge)).thenReturn(jellyFile);
    	
    	when(jenkins.runJellyScript(jellyFile, badge)).thenThrow(new JellyException("Error rendering jelly"));
    	
        return this;
    }

    @Override
    public BuildBadgeAction get() {
        return badge;
    }
}