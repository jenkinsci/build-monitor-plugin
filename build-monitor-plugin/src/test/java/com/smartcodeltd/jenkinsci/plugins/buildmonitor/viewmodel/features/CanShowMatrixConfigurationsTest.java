package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.JobStateRecipe;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.MatrixConfigurationStateRecipe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.Sugar.*;
import static hudson.model.Result.SUCCESS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CanShowMatrixConfigurationsTest {
    private JobView job;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_know_if_a_failing_build_has_been_claimed() throws Exception {
        List<JobStateRecipe> matrixConfigurations = Arrays.asList(
            new MatrixConfigurationStateRecipe().withName("A").whereTheLast(build().finishedWith(SUCCESS)),
            new MatrixConfigurationStateRecipe().withName("B").whereTheLast(build().finishedWith(SUCCESS))
        );

        job = a(jobView().which(new ShowMatrixConfigurations()).of(a(matrixProject(matrixConfigurations))));

        assertThat(job.getJobs().size(), is(2));
    }
}
