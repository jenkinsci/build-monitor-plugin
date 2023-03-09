package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config.DisplayOptions;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.CanBeClaimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.CanBeDiagnosedForProblems;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.Feature;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasBadgesBadgePlugin;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasHeadline;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.HasJunitRealtime;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.KnowsCurrentBuildsDetails;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.KnowsLastCompletedBuildDetails;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.headline.HeadlineConfig;
import hudson.model.Job;
import java.util.ArrayList;
import java.util.List;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;

/**
 * @author Jan Molak
 */
public class JobViews {
    private static final String Claim                       = "claim";
    private static final String Build_Failure_Analyzer      = "build-failure-analyzer";
    private static final String Badge_Plugin                = "badge";
    private static final String Pipeline                    = "workflow-job";
    private static final String Junit_Realtime              = "junit-realtime-test-reporter";

    private final StaticJenkinsAPIs jenkins;
    private final com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config config;

    public JobViews(StaticJenkinsAPIs jenkins, com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config config) {
        this.jenkins = jenkins;
        this.config  = config;
    }

    public JobView viewOf(Job<?, ?> job) {
        List<Feature> viewFeatures = new ArrayList<>();

        // todo: a more elegant way of assembling the features would be nice
        viewFeatures.add(new HasConfig(config));
        viewFeatures.add(new HasHeadline(new HeadlineConfig(config.shouldDisplayCommitters())));
        viewFeatures.add(new KnowsLastCompletedBuildDetails());
        viewFeatures.add(new KnowsCurrentBuildsDetails());

        if (jenkins.hasPlugin(Claim)) {
            viewFeatures.add(new CanBeClaimed());
        }

        if (jenkins.hasPlugin(Build_Failure_Analyzer)) {
            viewFeatures.add(new CanBeDiagnosedForProblems(config.getBuildFailureAnalyzerDisplayedField()));
        }

        if (config.getDisplayBadges() != DisplayOptions.Never) {
            if (jenkins.hasPlugin(Badge_Plugin)) {
                viewFeatures.add(new HasBadgesBadgePlugin(config));
            }
        }

        if (config.shouldDisplayJUnitProgress() && jenkins.hasPlugin(Junit_Realtime)) {
            viewFeatures.add(new HasJunitRealtime());
        }

        boolean isPipelineJob = jenkins.hasPlugin(Pipeline) && job instanceof WorkflowJob;

        return JobView.of(job, viewFeatures, isPipelineJob);
    }
}
