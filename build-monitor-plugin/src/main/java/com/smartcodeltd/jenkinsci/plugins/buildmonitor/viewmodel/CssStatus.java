package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.Result;

import java.util.HashMap;
import java.util.Map;

import static hudson.model.Result.*;

/**
 * @author Jan Molak
 */
public class CssStatus {

    private static final Map<Result, String> statuses = new HashMap<Result, String>() {{
        put(SUCCESS,   "successful");
        put(UNSTABLE,  "unstable");
        put(FAILURE,   "failing");
        put(NOT_BUILT, "unknown");
        put(ABORTED,   "failing");  // if someone has aborted it then something is clearly not right, right? :)
    }};

    public static String of(final JobView job) {
        String status = statusOf(job.lastResult());

        if (job.isDisabled()) {
            status += " disabled";
        }

        if (job.isRunning()) {
            status += " running";
        }

        if (job.isClaimed()) {
            status += " claimed";
        }

        return status;
    }

    private static String statusOf(Result result) {
        return statuses.containsKey(result) ? statuses.get(result) : "unknown";
    }
}
