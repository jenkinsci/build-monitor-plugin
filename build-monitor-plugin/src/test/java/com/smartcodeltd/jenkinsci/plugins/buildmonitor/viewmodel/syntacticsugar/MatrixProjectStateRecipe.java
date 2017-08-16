package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import hudson.matrix.MatrixProject;
import hudson.model.Job;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;

class MatrixProjectStateRecipe extends JobStateRecipe {
    private List<JobStateRecipe> matrixConfigurationStateRecipes;

    public MatrixProjectStateRecipe(List<JobStateRecipe> matrixConfigurationStateRecipes) {
        super();

        job = mock(MatrixProject.class);
        this.matrixConfigurationStateRecipes = matrixConfigurationStateRecipes;

        Mockito.doReturn(extractJobs(this.matrixConfigurationStateRecipes)).when(job).getAllJobs();
    }

    private Collection<Job<?, ?>> extractJobs(List<JobStateRecipe> matrixConfigurationStateRecipes) {
        List<Job<?,?>> jobs = new ArrayList<Job<?, ?>>();

        for (JobStateRecipe matrixConfigurationStateRecipe : matrixConfigurationStateRecipes) {
            jobs.add(matrixConfigurationStateRecipe.job);
        }

        return jobs;
    }
}