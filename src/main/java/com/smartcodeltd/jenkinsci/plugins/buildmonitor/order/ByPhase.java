package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import java.util.List;
import hudson.model.AbstractProject;
import hudson.model.Job;
import hudson.model.Hudson;
import hudson.model.DependencyGraph;
import hudson.tasks.Builder;
import com.tikal.jenkins.plugins.multijob.MultiJobProject;
import com.tikal.jenkins.plugins.multijob.MultiJobBuilder;
import com.tikal.jenkins.plugins.multijob.PhaseJobsConfig;

import java.util.Comparator;

public class ByPhase implements Comparator<Job<?, ?>> {
    @Override
    public int compare(Job<?, ?> a, Job<?,?> b) {
        int aphase = this.phaseof(a);
        int bphase = this.phaseof(b);
        return aphase - bphase;
    }

    private int phaseof(Job<?, ?> job) {
        AbstractProject project = (AbstractProject) job;
        DependencyGraph depsgraph = Hudson.getInstance().getDependencyGraph();
        MultiJobProject parent = null;
        for (final AbstractProject<?, ?> p : depsgraph.getUpstream(project)) {
            if (p instanceof MultiJobProject) {
                parent = (MultiJobProject) p;
                break;
            }
        }
        if (parent != null) {
            List<Builder> builders = parent.getBuilders();
            if (builders != null) {
                int phase = 1;
                for (Builder builder : builders) {
                    if (builder instanceof MultiJobBuilder) {
                        MultiJobBuilder multiJobBuilder = (MultiJobBuilder) builder;
                        List<PhaseJobsConfig> phaseJobs = multiJobBuilder.getPhaseJobs();
                        for (PhaseJobsConfig phaseJob : phaseJobs) {
                            if (phaseJob.getJobName().equals(project.getDisplayName())) {
                                return phase;
                            }
                        }
                        phase++;
                    }
                }
            }
        }
        return 0;
    }
}
