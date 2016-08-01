package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.*;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasHeadline.CurrentState.*;
import static hudson.model.Result.NOT_BUILT;
import static hudson.model.Result.SUCCESS;

/**
 * @author Jan Molak
 */
public class HasHeadline implements Feature<Headline> {

    private final HeadlineConfig config;
    private JobView job;

    public HasHeadline(HeadlineConfig config) {
        this.config = config;
    }

    @Override
    public HasHeadline of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Headline asJson() {
        return headlineOf(job).asJson();
    }

    private SerialisableAsJsonObjectCalled<Headline> headlineOf(JobView job) {
        switch (stateOf(job)) {
            case Never_Run:  return new NoHeadline();
            case Successful: return new HeadlineOfSuccessful(job, config);
            case Failed:     return new HeadlineOfFailing(job, config);
            default:         return new NoHeadline();
        }
    }

    private CurrentState stateOf(JobView job) {

        if (SUCCESS.equals(job.lastCompletedBuild().result())) {
            return Successful;
        }

        if (NOT_BUILT.equals(job.lastBuild().result())) {
            return Never_Run;
        }

        return Failed;
    }

    public enum CurrentState {
        Never_Run,
        Successful,
        Failed;
    }
}
