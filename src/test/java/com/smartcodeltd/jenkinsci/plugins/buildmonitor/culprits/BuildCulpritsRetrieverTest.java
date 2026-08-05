package com.smartcodeltd.jenkinsci.plugins.buildmonitor.culprits;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.pipeline.PipelineHelper;
import hudson.Plugin;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.User;
import hudson.scm.ChangeLogSet;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class BuildCulpritsRetrieverTest {

    private MockedStatic<Jenkins> mockedJenkins;
    private StaticJenkinsAPIs staticJenkinsAPIs;

    @BeforeEach
    void setUp() throws Exception {
        // PipelineHelper caches class-lookup results in static fields; reset between tests
        // to prevent ordering-dependent failures.
        resetPipelineHelperCache();

        mockedJenkins = mockStatic(Jenkins.class);
        Jenkins jenkins = mock(Jenkins.class);
        mockedJenkins.when(Jenkins::get).thenReturn(jenkins);
        when(jenkins.getPlugin("workflow-job")).thenReturn(mock(Plugin.class));
        staticJenkinsAPIs = new StaticJenkinsAPIs();
    }

    @AfterEach
    void tearDown() {
        mockedJenkins.close();
    }

    @Test
    void getCommitters_follows_upstream_cause_to_freestyle_build_without_throwing() {
        // Regression test: previously threw ClassCastException because
        // BuildCulpritsWorkflowRun.getCommittersForRun was used on the upstream FreeStyleBuild.

        // Build the ChangeLogSet before opening any Mockito stubbing: changeSetWith() calls
        // mock() internally, which conflicts with an already-open when().thenReturn() chain.
        ChangeLogSet<?> aliceSet = changeSetWith("Alice");
        AbstractBuild<?, ?> upstreamBuild = mock(AbstractBuild.class);
        doReturn(aliceSet).when(upstreamBuild).getChangeSet();
        when(upstreamBuild.getCause(Cause.UpstreamCause.class)).thenReturn(null);

        WorkflowRun pipelineRun = pipelineRunWithNoCommitters();
        Cause.UpstreamCause upstreamCause = mock(Cause.UpstreamCause.class);
        doReturn(upstreamBuild).when(upstreamCause).getUpstreamRun();
        when(pipelineRun.getCause(Cause.UpstreamCause.class)).thenReturn(upstreamCause);

        Set<String> committers = BuildCulpritsRetriever.getInstanceForRun(pipelineRun, staticJenkinsAPIs)
                .getCommitters(pipelineRun);

        assertThat(committers, containsInAnyOrder("Alice"));
    }

    @Test
    void getCommitters_returns_pipeline_own_committers_without_following_upstream() {
        WorkflowRun pipelineRun = mock(WorkflowRun.class);
        setChangeSets(pipelineRun, changeSetWith("Bob"));

        Set<String> committers = BuildCulpritsRetriever.getInstanceForRun(pipelineRun, staticJenkinsAPIs)
                .getCommitters(pipelineRun);

        assertThat(committers, containsInAnyOrder("Bob"));
    }

    @Test
    void getCommitters_returns_empty_when_freestyle_build_has_no_committers_and_no_upstream() {
        AbstractBuild<?, ?> build = mock(AbstractBuild.class);
        when(build.getChangeSet()).thenReturn(null);
        when(build.getCause(Cause.UpstreamCause.class)).thenReturn(null);

        Set<String> committers = BuildCulpritsRetriever.getInstanceForRun(build, staticJenkinsAPIs)
                .getCommitters(build);

        assertThat(committers, empty());
    }

    private WorkflowRun pipelineRunWithNoCommitters() {
        WorkflowRun run = mock(WorkflowRun.class);
        when(run.getChangeSets()).thenReturn(List.of());
        return run;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setChangeSets(WorkflowRun run, ChangeLogSet changeSet) {
        when(run.getChangeSets()).thenReturn(List.of(changeSet));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private ChangeLogSet changeSetWith(String... authorNames) {
        List<ChangeLogSet.Entry> entries = new ArrayList<>();
        for (String name : authorNames) {
            User user = mock(User.class);
            when(user.getFullName()).thenReturn(name);
            ChangeLogSet.Entry entry = mock(ChangeLogSet.Entry.class);
            when(entry.getAuthor()).thenReturn(user);
            entries.add(entry);
        }
        return new ChangeLogSet(null, null) {
            @Override
            public boolean isEmptySet() {
                return entries.isEmpty();
            }

            @Override
            public Iterator iterator() {
                return entries.iterator();
            }
        };
    }

    private static void resetPipelineHelperCache() throws Exception {
        for (String fieldName : List.of("hasWorkflowClass", "workflowRunClass")) {
            Field field = PipelineHelper.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, null);
        }
    }
}
