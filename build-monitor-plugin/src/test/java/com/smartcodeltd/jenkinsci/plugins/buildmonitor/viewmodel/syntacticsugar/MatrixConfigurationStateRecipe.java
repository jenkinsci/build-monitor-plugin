package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import hudson.matrix.MatrixConfiguration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatrixConfigurationStateRecipe extends JobStateRecipe {
    public MatrixConfigurationStateRecipe() {
        super();
        job = mock(MatrixConfiguration.class);
    }

    @Override
    public MatrixConfigurationStateRecipe withName(String name) {
        when(job.getName()).thenReturn(name);

        return this;
    }
}