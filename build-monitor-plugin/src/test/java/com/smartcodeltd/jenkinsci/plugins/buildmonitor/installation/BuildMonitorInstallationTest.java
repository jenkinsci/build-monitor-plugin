package com.smartcodeltd.jenkinsci.plugins.buildmonitor.installation;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BuildMonitorInstallationTest {
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlEPjwjG6Q3ILlr91qlSxvvys046hUrhFmc8ibz9WvWEJKVs5dS/mCnRV4QFg4w3qoCH2lzuoSNHB7tWBYQXj1ZtbDiwjnFNYw1TyZBL43m2bRYQGjpcvHAUB6u07C7mmoehpaYoFkpJbLEYEGXsKV/0bY22n00cZHwoTGl5biGVX8gvNKq604pK97jdVuBNZeXiOMXS00Yrwv8tgWeRDIUhUCO8T4rj0bBULh1Pyg/aJBsts7z5RydB+Nb5OZfAfaklVTpFld+ZHMjt0Q7VYhkMkbBD+ZFRkHvRNqg2q7wdQFScGGx7G2RstUtcIPv5Ga36fj1okDU6c2vyPHErTuwIDAQAB";

    @Test
    public void helps_to_avoid_duplicated_stats_and_keep_jenkins_instance_anonymous() throws Exception {
        BuildMonitorInstallation installation = new BuildMonitorInstallation(withPublicKey(PUBLIC_KEY));

        assertThat(installation.anonymousCorrelationId(), is(not(PUBLIC_KEY)));
        assertThat(installation.anonymousCorrelationId().length(), is(64));  // sha 256
    }

    @Test
    public void only_calculates_the_correlation_hash_once() throws Exception {
        StaticJenkinsAPIs jenkinsAPIs = withPublicKey(PUBLIC_KEY);

        BuildMonitorInstallation installation = new BuildMonitorInstallation(jenkinsAPIs);

        installation.anonymousCorrelationId();
        installation.anonymousCorrelationId();

        verify(jenkinsAPIs, times(1)).encodedPublicKey();
    }

    // --

    private StaticJenkinsAPIs withPublicKey(String publicKey) {
        StaticJenkinsAPIs jenkins = mock(StaticJenkinsAPIs.class);
        when(jenkins.encodedPublicKey()).thenReturn(publicKey);

        return jenkins;
    }
}