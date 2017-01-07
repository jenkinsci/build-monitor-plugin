package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import com.sonyericsson.jenkins.plugins.bfa.model.FoundFailureCause;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class CanBeDiagnosedForProblems implements Feature<CanBeDiagnosedForProblems.Problems> {
    private JobView job;

    public CanBeDiagnosedForProblems() {

    }

    @Override
    public CanBeDiagnosedForProblems of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Problems asJson() {
        Optional<FailureCauseBuildAction> details = job.lastCompletedBuild().detailsOf(FailureCauseBuildAction.class);

        return details.isPresent()                  // would be nice to have .map(Claim(_)).orElse(), but hey...
                ? new Problems(details.get())
                : null;                             // `null` because we don't want to serialise an empty object
    }

    public static class Problems {

        private final List<String> failures = newArrayList();

        public Problems(FailureCauseBuildAction action) {
            for (FoundFailureCause failure : action.getFoundFailureCauses()) {
                failures.add(failure.getName());
            }
        }

        @JsonValue
        public List<String> value() {
            return ImmutableList.copyOf(failures);
        }
    }
}
