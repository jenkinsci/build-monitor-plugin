package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private CandidateHeadline headlineOf(final JobView job) {
        List<CandidateHeadline> availableHeadlines = new ArrayList<>();
        Collections.addAll(availableHeadlines,
                new HeadlineOfExecuting(job, config),
                new HeadlineOfAborted(job, config),
                new HeadlineOfFixed(job, config),
                new HeadlineOfFailing(job, config)
        );

        return availableHeadlines.stream()
                .filter(candidateHeadline -> candidateHeadline.isApplicableTo(job))
                .findFirst()
                .orElse(new NoHeadline());
    }
}
