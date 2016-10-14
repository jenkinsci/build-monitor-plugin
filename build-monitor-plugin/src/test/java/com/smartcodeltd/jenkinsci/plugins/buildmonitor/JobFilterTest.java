package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.cloudbees.hudson.plugins.folder.AbstractFolder;
import hudson.model.ItemGroup;
import hudson.model.Job;
import hudson.model.TopLevelItem;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobFilterTest {
    @Test
    public void shouldHandleNull() {
        JobFilter filter = configuredJobFilter(null, null);
        List<Job<?, ?>> jobs = filter.filterJobs(null);
        assertThat("should be empty", jobs.isEmpty(), is(true));
    }

    @Test
    public void shouldHandleFolderWithNullItems() {
        JobFilter filter = configuredJobFilter(null, "nope");
        final AbstractFolder folder = mockedFolderWithNullItems();
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.singleton(folder));
        assertThat("should be empty", jobs.isEmpty(), is(true));
    }

    @Test
    public void shouldNotAddStuff() {
        JobFilter filter = configuredJobFilter(null, null);
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.<TopLevelItem>emptySet());
        assertThat("should be empty", jobs.isEmpty(), is(true));
    }

    @Test
    public void shouldCopyDirectJobs() {
        JobFilter filter = configuredJobFilter(null, null);
        final Job<?, ?> job = mockedJob("foo");
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.singleton(job));
        assertThat("should contain one element", jobs.size(), is(1));
        assertThat(jobs.get(0), jobIsSame(job));
    }

    @Test
    public void shouldIncludeJobsOfFoldersByDefault() {
        JobFilter filter = configuredJobFilter(null, null);
        final Job<?, ?> job = mockedJob("job");
        final AbstractFolder folder = mockedFolder(job);
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.singleton(folder));
        assertThat("should contain one element", jobs.size(), is(1));
        assertThat(jobs.get(0), jobIsSame(job));
    }

    @Test
    public void shouldNotIncludeJobsThatDontMatchTheIncludePattern() {
        JobFilter filter = configuredJobFilter("special", null);
        final Job<?, ?> job = mockedJob("job");
        final AbstractFolder folder = mockedFolder(job);
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.singleton(folder));
        assertThat("should be empty", jobs.isEmpty(), is(true));
    }

    @Test
    public void shouldExcludeMatchingJobs() {
        JobFilter filter = configuredJobFilter(null, "nope");
        final Job<?, ?> job = mockedJob("nope");
        final AbstractFolder folder = mockedFolder(job);
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.singleton(folder));
        assertThat("should be empty", jobs.isEmpty(), is(true));
    }

    @Test
    public void excludeShouldWin() {
        JobFilter filter = configuredJobFilter("a", "b");
        final Job<?, ?> job = mockedJob("ab");
        final AbstractFolder folder = mockedFolder(job);
        List<Job<?, ?>> jobs = filter.filterJobs(Collections.singleton(folder));
        assertThat("should be empty", jobs.isEmpty(), is(true));
    }

    private Job<?, ?> mockedJob(String relativeName) {
        final Job<?, ?> job = mock(Job.class);
        when(job.getRelativeNameFrom((ItemGroup) anyObject())).thenReturn(relativeName);
        return job;
    }

    private AbstractFolder mockedFolder(Job<?, ?>... jobs) {
        final AbstractFolder folder = mock(AbstractFolder.class);
        when(folder.getItems()).thenReturn(Arrays.asList(jobs));
        return folder;
    }

    private AbstractFolder mockedFolderWithNullItems() {
        final AbstractFolder folder = mock(AbstractFolder.class);
        when(folder.getItems()).thenReturn(null);
        return folder;
    }

    private JobFilter configuredJobFilter(String include, String exclude)  {
        Config config = new Config();
        config.setBranchesToInclude(include);
        config.setBranchesToExclude(exclude);
        return new JobFilter(config);
    }

    private static Matcher<Job> jobIsSame(Job job) {
        return new JobIsSame(job);
    }

    static class JobIsSame extends BaseMatcher<Job> {
        private final Job<?, ?> job;

        JobIsSame(Job<?, ?> job) {
            this.job = job;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("object was not the same as the expected one");
        }

        @Override
        public boolean matches(Object item) {
            return item == job;
        }
    }
}
