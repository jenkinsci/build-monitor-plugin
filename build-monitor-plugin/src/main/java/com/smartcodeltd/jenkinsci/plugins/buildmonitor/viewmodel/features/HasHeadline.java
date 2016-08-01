package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.base.Predicate;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.*;

import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Lists.newArrayList;

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
        List<CandidateHeadline> availableHeadlines = newArrayList(
                new HeadlineOfExecuting(job, config),
                new HeadlineOfFixed(job, config),
                new HeadlineOfFailing(job, config)
        );

        return getFirst(filter(availableHeadlines, new Predicate<CandidateHeadline>() {
            @Override
            public boolean apply(CandidateHeadline candidateHeadline) {
                return candidateHeadline.isApplicableTo(job);
            }
        }), new NoHeadline());
    }
}
