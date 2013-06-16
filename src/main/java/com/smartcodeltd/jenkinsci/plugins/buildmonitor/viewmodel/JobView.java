package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import hudson.model.*;

import java.util.ArrayList;
import java.util.List;

public class JobView {
    private Job<?, ?> job;

    public static JobView of(Job<?, ?> job) {
        return new JobView(job);
    }

    public String name() {
        return job.getName();
    }

    public String status() {
        String status = isSuccessful() ? "successful" : "failing";

        if (isRunning()) {
            status += " running";
        }

        return status;
    }

    public List<String> culprits() {
        List<String> culprits = new ArrayList();

        Run<?, ?> run = job.getLastBuild();

        while (run != null) {
            if (run instanceof AbstractBuild<?, ?>) {
                AbstractBuild<?, ?> build = (AbstractBuild<?, ?>) run;
                for (User culprit : build.getCulprits()) {
                    culprits.add(culprit.getFullName());
                }
            }
            run = run.getPreviousBuild();

            // only look for culprits in broken builds
            if (run != null && Result.SUCCESS.equals(run.getResult())) {
                run = null;
            }
        }

        return culprits;
    }


    private JobView(Job<?, ?> job) {
        this.job = job;
    }

    private Result lastResult() {
        Run<?, ?> lastBuild = job.getLastBuild();
        if (isRunning()) {
            lastBuild = lastBuild.getPreviousBuild();
        }

        return lastBuild != null
                ? lastBuild.getResult()
                : Result.NOT_BUILT;
    }

    private boolean isSuccessful() {
        return lastResult() == Result.SUCCESS;
    }

    private boolean isRunning() {
        Run<?, ?> lastBuild = job.getLastBuild();

        return (lastBuild != null)
               && (lastBuild.hasntStartedYet() || lastBuild.isBuilding() || lastBuild.isLogUpdated());
    }

    public String toString() {
        return "[[ " + name() + ": " + lastResult() + " running:" + isRunning() +" ]]";
    }
}
