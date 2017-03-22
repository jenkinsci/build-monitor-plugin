package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

@PrepareForTest({URL.class})
public class HasBadgesTest {
    private JobView job;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_support_job_without_badges() throws Exception {
    	job = a(jobView().which(new HasBadges()).of(
                a(job())));

        assertThat(serialisedBadgesDetailsOf(job), is(nullValue()));
    }

    @Test
    public void should_convert_badges_to_json() throws Exception {
    	job = a(jobView().which(new HasBadges()).of(
                a(job().whereTheLast(build().hasBadges(badge().withText("badge1"), badge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(2));
    }

    @Test
    public void should_ignore_badges_with_icon() throws Exception {
    	job = a(jobView().which(new HasBadges()).of(
                a(job().whereTheLast(build().hasBadges(badge().withIcon("icon.gif", "badge1"), badge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    @Test
    public void should_report_badges_from_latest_build() throws Exception {
    	job = a(jobView().which(new HasBadges()).of(
                a(job().whereTheLast(build().isStillBuilding().hasBadges(badge().withText("badge1")))
                		.andThePrevious(build().hasBadges(badge().withText("badge1"), badge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    private HasBadges.Badges serialisedBadgesDetailsOf(JobView job) {
        return job.which(HasBadges.class).asJson();
    }
}
