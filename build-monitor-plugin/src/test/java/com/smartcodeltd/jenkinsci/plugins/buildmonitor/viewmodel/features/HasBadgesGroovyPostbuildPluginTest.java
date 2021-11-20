package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import jenkins.model.Jenkins;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockedStatic;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class HasBadgesGroovyPostbuildPluginTest {
    private JobView job;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @Before
    public void setup() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::getInstance).thenReturn(jenkins);
    }

    @After
    public void tearDown() {
        mockedJenkins.close();
    }

    @Test
    public void should_support_job_without_badges() throws Exception {
        job = a(jobView().which(new HasBadgesGroovyPostbuildPlugin()).of(a(job())));

        assertThat(serialisedBadgesDetailsOf(job), is(nullValue()));
    }

    @Test
    public void should_convert_badges_to_json() throws Exception {
        job = a(jobView().which(new HasBadgesGroovyPostbuildPlugin()).of(
            a(job().whereTheLast(build().hasBadgesGroovyPostbuildPlugin(groovyPostbuildBadge().withText("badge1"), groovyPostbuildBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(2));
    }

    @Test
    public void should_ignore_badges_with_icon() throws Exception {
        job = a(jobView().which(new HasBadgesGroovyPostbuildPlugin()).of(
            a(job().whereTheLast(build().hasBadgesGroovyPostbuildPlugin(groovyPostbuildBadge().withIcon("icon.gif", "badge1"), groovyPostbuildBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    @Test
    public void should_report_badges_from_latest_build() throws Exception {
        job = a(jobView().which(new HasBadgesGroovyPostbuildPlugin()).of(
                a(job().whereTheLast(build().isStillBuilding().hasBadgesGroovyPostbuildPlugin(groovyPostbuildBadge().withText("badge1")))
                        .andThePrevious(build().hasBadgesGroovyPostbuildPlugin(groovyPostbuildBadge().withText("badge1"), groovyPostbuildBadge().withText("badge2"))))));

        assertThat(serialisedBadgesDetailsOf(job).value(), hasSize(1));
    }

    private HasBadgesGroovyPostbuildPlugin.Badges serialisedBadgesDetailsOf(JobView job) {
        return job.which(HasBadgesGroovyPostbuildPlugin.class).asJson();
    }
}
