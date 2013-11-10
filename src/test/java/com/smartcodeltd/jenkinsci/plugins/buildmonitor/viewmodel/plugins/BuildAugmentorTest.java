package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.NotClaimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar.BuildStateRecipe;
import hudson.model.Result;
import hudson.model.Run;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BuildAugmentorTest {
    private BuildAugmentor augmentor = new BuildAugmentor();
    private final String AUTHOR = "Adam";
    private final String REASON = "Sorry, I broke it, fixing now";

    private Run<?, ?> unclaimedBuild = a(build());
    private Run<?, ?> claimedBuild = a(build().finishedWith(Result.FAILURE).andWasClaimedBy(AUTHOR, REASON));

    @Test
    public void should_recognise_any_build_as_not_claimed_by_default() {
        assertThat(augmentor.detailsOf(unclaimedBuild, Claim.class), instanceOf(NotClaimed.class));
        assertThat(augmentor.detailsOf(claimedBuild, Claim.class),   instanceOf(NotClaimed.class));
    }

    @Test
    public void should_recognise_a_claimed_build_once_you_ask_him_to_do_it() throws Exception {
        augmentor.support(Claim.class);

        Claim claim = augmentor.detailsOf(claimedBuild, Claim.class);

        assertThat(claim, instanceOf(Claimed.class));

        assertThat(claim.wasMade(), is(true));
        assertThat(claim.author(),  is(AUTHOR));
        assertThat(claim.reason(),  is(REASON));
    }

    /*
     * Syntactic sugar
     */

    private Run<?, ?> a(BuildStateRecipe recipe) {
        return recipe.execute();
    }

    private BuildStateRecipe build() {
        return new BuildStateRecipe();
    }
}