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
public class BadgesDetailsTest {
    private JobView job;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_support_job_without_badges() throws Exception {
    	job = a(jobView().which(new BadgesDetails(jenkins())).of(
                a(job())));

        assertThat(serialisedBadgesDetailsOf(job), is(nullValue()));
    }

    @Test
    public void should_convert_badges_to_html() throws Exception {
    	job = a(jobView().which(new BadgesDetails(jenkins())).of(
                a(job().whereTheLast(build().hasBadge(badge().withJelly()).and().hasBadge(badge().withJelly())))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(2));
    }

    @Test
    public void should_ignore_badges_without_jelly() throws Exception {
    	job = a(jobView().which(new BadgesDetails(jenkins())).of(
                a(job().whereTheLast(build().hasBadge(badge().withoutJelly()).and().hasBadge(badge().withJelly())))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    @Test
    public void should_ignore_conversion_errors() throws Exception {
    	job = a(jobView().which(new BadgesDetails(jenkins())).of(
                a(job().whereTheLast(build().hasBadge(badge().withInvalidJelly()).and().hasBadge(badge().withJelly())))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    private BadgesDetails.Badges serialisedBadgesDetailsOf(JobView job) {
        return job.which(BadgesDetails.class).asJson();
    }
}
