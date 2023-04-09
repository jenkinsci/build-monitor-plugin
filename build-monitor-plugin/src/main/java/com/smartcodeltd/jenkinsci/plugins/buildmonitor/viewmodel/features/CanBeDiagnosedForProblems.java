package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.fasterxml.jackson.annotation.JsonValue;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.Config.BuildFailureAnalyzerDisplayedField;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import com.sonyericsson.jenkins.plugins.bfa.model.FoundFailureCause;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CanBeDiagnosedForProblems implements Feature<CanBeDiagnosedForProblems.Problems> {
    private JobView job;
    private BuildFailureAnalyzerDisplayedField displayedField;

    public CanBeDiagnosedForProblems(BuildFailureAnalyzerDisplayedField displayedField) {
        this.displayedField = displayedField;
    }

    @Override
    public CanBeDiagnosedForProblems of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Problems asJson() {
        Optional<FailureCauseBuildAction> details = job.lastCompletedBuild().detailsOf(FailureCauseBuildAction.class);

        return details.map(failureCauseBuildAction -> new Problems(failureCauseBuildAction, displayedField))
                .orElse(null); // `null` because we don't want to serialise an empty object
    }

    public static class Problems {

        private final List<String> failures = new ArrayList<>();

        public Problems(FailureCauseBuildAction action, BuildFailureAnalyzerDisplayedField displayedField) {
            if (displayedField != BuildFailureAnalyzerDisplayedField.None) {
                for (FoundFailureCause failure : action.getFoundFailureCauses()) {
                    String str = displayedField == BuildFailureAnalyzerDisplayedField.Description
                            ? failure.getDescription()
                            : failure.getName();
                    if (str != null) {
                        failures.add(str);
                    }
                }
            }
        }

        @JsonValue
        public List<String> value() {
            return List.copyOf(failures);
        }
    }
}
