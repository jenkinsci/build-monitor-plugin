package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.Result;

import java.util.Map;

import static hudson.model.Result.*;

/**
 * @author Jan Molak
 */
public class CssStatus {

    private static final Map<Result, String> statuses =
            Map.of(
                    SUCCESS, "successful",
                    UNSTABLE, "unstable",
                    FAILURE, "failing",
                    NOT_BUILT, "unknown",
                    ABORTED, "aborted");

    public static String of(final JobView job) {
        Result result = job.lastResult();
        String status;
        if (result == null) {
            status = "unknown";
        } else {
            status = statuses.getOrDefault(result, "unknown");
        }

        if (job.isDisabled()) {
            status += " disabled";
        }

        if (job.isRunning()) {
            status += " running";
        }

        return status;
    }
}
