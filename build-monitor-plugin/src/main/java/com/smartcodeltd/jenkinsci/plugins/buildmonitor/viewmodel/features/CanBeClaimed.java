package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.plugins.claim.ClaimBuildAction;

import java.util.Optional;

public class CanBeClaimed implements Feature {
    private JobView job;

    @Override
    public CanBeClaimed of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Claim asJson() {
        Optional<ClaimBuildAction> details = job.lastCompletedBuild().detailsOf(ClaimBuildAction.class);

        return details.isPresent()                  // would be nice to have .map(Claim(_)).orElse(), but hey...
                ? new Claim(details.get())
                : null;                             // `null` because we don't want to serialise an empty object

    }

    public static class Claim {

        private final ClaimBuildAction details;

        public Claim(ClaimBuildAction details) {
            this.details = details;
        }

        @JsonProperty
        public boolean isActive() {
            return details.isClaimed();
        }

        @JsonProperty
        public String author() {
            return details.getClaimedByName();
        }

        @JsonProperty
        public String reason() {
            return details.getReason();
        }
    }
}
