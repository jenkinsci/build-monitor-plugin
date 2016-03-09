package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa;

import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import com.sonyericsson.jenkins.plugins.bfa.model.FoundFailureCause;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnalysedTest {
    private final static String FAILURE1_NAME = "JMS Server Down";
    private final static String FAILURE2_NAME = "Unit test failures";

    @Test
    public void should_be_a_simple_proxy_to_the_claim_build_action() throws Exception {
        Analysis analysis = new Analysed(failureCauseBuildAction(FAILURE1_NAME,FAILURE2_NAME));

        assertThat(analysis.foundKnownFailures(), is(true));
        assertThat(analysis.failures(), contains(FAILURE1_NAME, FAILURE2_NAME));
    }

    public static FailureCauseBuildAction failureCauseBuildAction(String... FailureNames) {
        FailureCauseBuildAction action = mock(FailureCauseBuildAction.class);
        List<FoundFailureCause> items = new ArrayList<FoundFailureCause>();
        for( String name : FailureNames ) {
            items.add(failure(name));
        }
        when(action.getFoundFailureCauses()).thenReturn(items);

        return action;
    }

    private static FoundFailureCause failure(String name) {
        FoundFailureCause failure = mock(FoundFailureCause.class);
        when(failure.getName()).thenReturn(name);
        return failure;
    }
}