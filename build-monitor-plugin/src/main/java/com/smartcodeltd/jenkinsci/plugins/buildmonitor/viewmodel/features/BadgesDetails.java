package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.collect.ImmutableList;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorLogger;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

import hudson.model.BuildBadgeAction;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.jelly.JellyException;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author Daniel Beland
 */
public class BadgesDetails implements Feature<BadgesDetails.Badges> {
	private JobView job;

	private final StaticJenkinsAPIs jenkins;

	public BadgesDetails(StaticJenkinsAPIs jenkinsAPIs) {
		this.jenkins = jenkinsAPIs;
	}

	@Override
	public BadgesDetails of(JobView jobView) {
		this.job = jobView;

		return this;
	}

	@Override
	public Badges asJson() {
		return !job.lastCompletedBuild().badges().isEmpty()
				? new Badges(jenkins, job.lastCompletedBuild().badges())
						: null; // `null` because we don't want to serialise an empty object
	}

	public static class Badges {
		private static final BuildMonitorLogger logger = BuildMonitorLogger.forClass(Badges.class);

		private final List<String> badges = newArrayList();

		public Badges(StaticJenkinsAPIs jenkins, List<BuildBadgeAction> badgeActions) {
			render(jenkins, badgeActions);
		}

		private void render(StaticJenkinsAPIs jenkins, List<BuildBadgeAction> badgeActions) {
			for( BuildBadgeAction badge : badgeActions ) {
				URL jellyFile = jenkins.getBadgeJelly(badge);

				if( jellyFile != null ) {
					try {
						String html = new String(jenkins.runJellyScript(jellyFile, badge)); 
						badges.add(html);
					} catch (IOException ex) {
						// This can produce a lot of logs as the same badge will constantly 
						// fail every time we refresh the display so using debug level 
						logger.debug("render", "Error rendering Build Badge: {0}", ex.getMessage());
					} catch (JellyException ex) {
						logger.debug("render", "Error rendering Build Badge: {0}", ex.getMessage());
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
