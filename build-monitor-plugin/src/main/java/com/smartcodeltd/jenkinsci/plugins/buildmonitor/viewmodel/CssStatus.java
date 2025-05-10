package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.Result;
import java.util.Map;

/**
 * @author Jan Molak
 */
public class CssStatus {

    private static final Map<Result, String> statuses = Map.of(
            Result.SUCCESS, "successful",
            Result.UNSTABLE, "unstable",
            Result.FAILURE, "failing",
            Result.NOT_BUILT, "unknown",
            Result.ABORTED, "aborted");

    public static String of(final JobView job) {
        Result result = job.lastResult();
        String status;
        if (result == null) {
            status = "unknown";
        } else {
            status = statuses.getOrDefault(result, "unknown");
        }

        if (job.isRunning()) {
            status = "running";
        }

        return status;
    }
}
