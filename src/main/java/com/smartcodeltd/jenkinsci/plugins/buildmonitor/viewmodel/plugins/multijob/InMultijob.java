package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob;

import java.util.List;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import com.tikal.jenkins.plugins.multijob.MultiJobProject;
import com.tikal.jenkins.plugins.multijob.MultiJobBuilder;
import com.tikal.jenkins.plugins.multijob.PhaseJobsConfig;

public class InMultijob implements Multijob {
    private final int _phase;
    private final int _jobsinphase;
    private final int _numphases;

    public InMultijob(AbstractProject<?, ?> project, MultiJobProject parent) {
        List<Builder> builders = parent.getBuilders();
        if (builders != null) {
            int _phase = 0;
            int _jobsinphase = 0;
            int numphases = 0;
            for (Builder builder : builders) {
                if (builder instanceof MultiJobBuilder) {
                    numphases++;
                    MultiJobBuilder multiJobBuilder = (MultiJobBuilder) builder;
                    List<PhaseJobsConfig> phaseJobs = multiJobBuilder
                        .getPhaseJobs();
                    int phase = 1;
                    for (PhaseJobsConfig phaseJob : phaseJobs) {
                        if (phaseJob.getJobName().equals(project.getDisplayName())) {
                            _phase = phase;
                            _jobsinphase = phaseJobs.size();
                        }
                    }
                    phase++;
                }
            }
            this._phase = _phase;
            this._jobsinphase = _jobsinphase;
            this._numphases = numphases;
        } else {
            this._phase = 0;
            this._jobsinphase = 1;
            this._numphases = 1;
        }
    }

    @Override
    public int phase() {
        return _phase;
    }

    @Override
    public int jobsinphase() {
        return _jobsinphase;
    }

    @Override
    public int numphases() {
        return _numphases;
    }
}
