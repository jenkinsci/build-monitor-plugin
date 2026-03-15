package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.a;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.build;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.job;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.jobView;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.model.Result;
import jenkins.model.Jenkins;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class CanBeClaimedTest {

    private JobView job;

    private MockedStatic<Jenkins> mockedJenkins;
    private Jenkins jenkins;

    @BeforeEach
    void beforeEach() {
        mockedJenkins = mockStatic(Jenkins.class);
        jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::get).thenReturn(jenkins);
    }

    @AfterEach
    void afterEach() {
        mockedJenkins.close();
    }

    /* TODO not working on recent versions of Mockito
    @Test
    void should_know_if_a_failing_build_has_been_claimed() {
        String ourPotentialHero = "Adam",
                theReason = "I broke it, sorry, fixing now";

        job = a(jobView().which(new CanBeClaimed()).of(
                a(job().whereTheLast(build().finishedWith(Result.FAILURE).and().wasClaimedBy(ourPotentialHero, theReason)))));

        assertThat(serialisedClaimOf(job).author(), is(ourPotentialHero));
        assertThat(serialisedClaimOf(job).reason(), is(theReason));
    }
    */

    @Test
    void should_know_if_a_failing_build_has_not_been_claimed() {
        job = a(jobView().which(new CanBeClaimed()).of(a(job().whereTheLast(build().finishedWith(Result.FAILURE)))));

        assertThat(serialisedClaimOf(job), is(nullValue()));
    }

    @Test
    void should_complain_if_the_build_was_not_claimable() {
        job = a(jobView().of(a(job().withName("my-project").whereTheLast(build().finishedWith(Result.FAILURE)))));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> job.which(CanBeClaimed.class));
        assertEquals("CanBeClaimed is not a feature of this project: 'my-project'", thrown.getMessage());
    }

    private CanBeClaimed.Claim serialisedClaimOf(JobView job) {
        return job.which(CanBeClaimed.class).asJson();
    }
}
