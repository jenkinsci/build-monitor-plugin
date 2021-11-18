package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class HasBadgesBadgePluginTest {
    private JobView job;
    
    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @Before
    public void setup() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::get).thenReturn(jenkins);
    }

    @After
    public void tearDown() {
        mockedJenkins.close();
    }

    @Test
    public void should_support_job_without_badges() {
        job = a(jobView().which(hasBadgePluginBadges(withDefaultConfig())).of(a(job())));

        assertThat(serialisedBadgesDetailsOf(job), is(nullValue()));
    }

    @Test
    public void should_convert_badges_to_json() {
        job = a(jobView().which(hasBadgePluginBadges(withDefaultConfig())).of(a(job().whereTheLast(
                build().hasBadges(badgePluginBadge().withText("badge1"), badgePluginBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(2));
    }

    @Test
    public void should_ignore_badges_with_icon() {
        job = a(jobView().which(hasBadgePluginBadges(withDefaultConfig()))
                .of(a(job().whereTheLast(build().hasBadges(badgePluginBadge().withIcon("icon.gif", "badge1"),
                        badgePluginBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    @Test
    public void should_report_badges_from_latest_build() {
        job = a(jobView().which(hasBadgePluginBadges(withDefaultConfig()))
                .of(a(job().whereTheLast(build().isStillBuilding().hasBadges(badgePluginBadge().withText("badge1")))
                        .andThePrevious(build().hasBadges(badgePluginBadge().withText("badge1"),
                                badgePluginBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    @Test
    public void should_report_badges_from_last_completed_build() {
        job = a(jobView().which(hasBadgePluginBadges(withConfig().withBadgesFromLastCompletedBuild()))
                .of(a(job().whereTheLast(build().isStillBuilding().hasBadges(badgePluginBadge().withText("badge1")))
                        .andThePrevious(build().hasBadges(badgePluginBadge().withText("badge1"),
                                badgePluginBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(2));
    }

    private HasBadgesBadgePlugin.Badges serialisedBadgesDetailsOf(JobView job) {
        return job.which(HasBadgesBadgePlugin.class).asJson();
    }
}
