package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.BuildStateRecipe;
import hudson.model.AbstractBuild;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuildViewTest {

    private static final String theName = "1.5+build.3";

    private BuildViewModel view;


    @Test
    public void should_know_the_name_of_the_job_its_based_on() {
        view = BuildView.of(aBuild(which().nameIs(theName)));

        assertThat(view.name(), is(theName));
        assertThat(view.toString(), is(theName));
    }


    private AbstractBuild<?, ?> aBuild(BuildStateRecipe recipe) {
        return recipe.execute();
    }

    private BuildStateRecipe which() {
        return new BuildStateRecipe();
    }
}