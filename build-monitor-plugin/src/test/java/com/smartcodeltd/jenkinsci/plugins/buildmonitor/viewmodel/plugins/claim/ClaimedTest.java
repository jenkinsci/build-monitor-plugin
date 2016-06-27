package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim;

import hudson.plugins.claim.ClaimBuildAction;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClaimedTest {
    private static final String AUTHOR = "Adam";
    private static final String REASON = "I broke it, sorry, fixing now.";
    private static final String EXPECTED_STRING_REPRESENTATION = String.format("Claimed by \"%s\": \"%s\"", AUTHOR, REASON);

    @Test
    public void should_be_a_simple_proxy_to_the_claim_build_action() throws Exception {
        Claim claim = new Claimed(claimBuildAction(AUTHOR, REASON));

        assertThat(claim.wasMade(),  is(true));
        assertThat(claim.author(),   is(AUTHOR));
        assertThat(claim.reason(),   is(REASON));

        assertThat(claim.toString(), is(EXPECTED_STRING_REPRESENTATION));
    }

    public static ClaimBuildAction claimBuildAction(String author, String reason) {
        ClaimBuildAction action = mock(ClaimBuildAction.class);
        when(action.isClaimed()).thenReturn(true);
        when(action.getClaimedByName()).thenReturn(author);
        when(action.getReason()).thenReturn(reason);

        return action;
    }
}