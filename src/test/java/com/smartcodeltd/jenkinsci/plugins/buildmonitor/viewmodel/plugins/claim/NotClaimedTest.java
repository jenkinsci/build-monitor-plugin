package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NotClaimedTest {
    @Test
    public void should_provide_sensible_defaults_if_the_build_is_not_claimed_or_the_claim_plugin_is_not_installed() throws Exception {
        Claim claim = new NotClaimed();

        assertThat(claim.toString(), is("Not claimed"));

        assertThat(claim.wasMade(), is(false));
        assertThat(claim.author(), is(nullValue()));
        assertThat(claim.reason(), is(nullValue()));
    }
}
