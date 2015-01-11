package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildsFilteringSettings;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.RelativeLocation;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.BuildAugmentor;
import hudson.model.*;
import hudson.triggers.TimerTrigger;
import hudson.util.RunList;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

import static hudson.model.Result.*;

/**
 * @author Jan Molak
 */
public class JobView {
    private final Date systemTime;
    private final Job<?, ?> job;
    private final BuildAugmentor augmentor;
    private final RelativeLocation relative;

    private final static Map<Result, String> statuses = new HashMap<Result, String>() {{
        put(SUCCESS,   "successful");
        put(UNSTABLE,  "unstable");
        put(FAILURE,   "failing");
        put(ABORTED,   "failing");  // if someone has aborted it then something is clearly not right, right? :)
    }};
    private final BuildsFilteringSettings filteringSettings;

    public static JobView of(Job<?, ?> job) {
        return new JobView(job, new BuildAugmentor(), RelativeLocation.of(job), new Date(), new BuildsFilteringSettings());
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), new BuildsFilteringSettings());
    }

    public static JobView of(Job<?, ?> job, RelativeLocation location) {
        return new JobView(job, new BuildAugmentor(), location, new Date(), new BuildsFilteringSettings());
    }

    public static JobView of(Job<?, ?> job, Date systemTime) {
        return new JobView(job, new BuildAugmentor(), RelativeLocation.of(job), systemTime, new BuildsFilteringSettings());
    }

    public static JobView of(Job<?, ?> job, BuildAugmentor augmentor, BuildsFilteringSettings filteringSettings) {
        return new JobView(job, augmentor, RelativeLocation.of(job), new Date(), filteringSettings);
    }

    @JsonProperty
    public String name() {
        return relative.name();
    }

    @JsonProperty
    public String url() {
        return relative.url();
    }

    private String statusOf(Result result) {
        return statuses.containsKey(result) ? statuses.get(result) : "unknown";
    }

    @JsonProperty
    public String status() {
        String status = statusOf(lastCompletedBuild().result());

        if (lastBuild().isRunning()) {
            status += " running";
        }

        if (lastCompletedBuild().isClaimed()) {
            status += " claimed";
        }

        return status;
    }

    @JsonProperty
    public String lastBuildName() {
        return lastBuild().name();
    }

    @JsonProperty
    public String lastBuildUrl() {
        return lastBuild().url();
    }

    @JsonProperty
    public String lastBuildDuration() {
        if (lastBuild().isRunning()) {
            return formatted(lastBuild().elapsedTime());
        }

        return formatted(lastBuild().duration());
    }

    @JsonProperty
    public String estimatedDuration() {
        return formatted(lastBuild().estimatedDuration());
    }

    private String formatted(Duration duration) {
        return null != duration
                ? duration.toString()
                : "";
    }

    @JsonProperty
    public int progress() {
        return lastBuild().progress();
    }

    @JsonProperty
    public boolean shouldIndicateCulprits() {
        return ! isClaimed() && culprits().size() > 0;
    }

    @JsonProperty
    public Set<String> culprits() {
        Set<String> culprits = new HashSet<String>();

        BuildViewModel build = lastBuild();
        // todo: consider introducing a BuildResultJudge to keep this logic in one place
        while (! SUCCESS.equals(build.result())) {
            culprits.addAll(build.culprits());

            if (! build.hasPreviousBuild()) {
                break;
            }

            build = build.previousBuild();
        };

        return culprits;
    }

    @JsonProperty
    public boolean isClaimed() {
        return lastCompletedBuild().isClaimed();
    }

    @JsonProperty
    public String claimAuthor() {
        return lastCompletedBuild().claimant();
    }

    @JsonProperty
    public String claimReason() {
        return lastCompletedBuild().reasonForClaim();
    }

    @JsonProperty
    public boolean hasKnownFailures() {
        return lastCompletedBuild().hasKnownFailures();
    }

    @JsonProperty
    public List<String> knownFailures() {
        return lastCompletedBuild().knownFailures();
    }

    public String toString() {
        return name();
    }


    private JobView(Job<?, ?> job, BuildAugmentor augmentor, RelativeLocation relative, Date systemTime, BuildsFilteringSettings filteringSettings) {
        this.job        = job;
        this.augmentor  = augmentor;
        this.systemTime = systemTime;
        this.relative   = relative;
        this.filteringSettings = filteringSettings;
    }

    private BuildViewModel lastBuild() {
        return buildViewOf(getLastSuitableBuild());
    }

    private String[] getAcceptableUsersList() {
        List<String>systemUsersIds = new ArrayList<String>();
        for (User user:User.getAll()) {
            systemUsersIds.add(user.getId());
        }

        Set<String> systemUsersIdsSet = new HashSet<String>(systemUsersIds);
        Set<String> specifiedUsers = new HashSet<String>(Arrays.asList(filteringSettings.getUsernames()));
        systemUsersIdsSet.retainAll(specifiedUsers);
        String[] result = systemUsersIdsSet.toArray(new String[systemUsersIdsSet.size()]);
        return result;
    }

    private boolean isShowBuild(List<Cause> causes, BuildsFilteringSettings filteringSettings) {
        String[] users = getAcceptableUsersList();
        for (Cause cause : causes) {
            if(cause instanceof Cause.UserIdCause) {
                Cause.UserIdCause userIdCause = ((Cause.UserIdCause) cause);
                String userId = userIdCause.getUserId();

                if(!filteringSettings.isShowAnonymousBuilds()) {
                    if(userId == null) {
                        return false;
                    }
                }

                if(userId != null && !Arrays.asList(users).contains(userId)) {
                    return false;
                }
            }
            if (cause instanceof TimerTrigger.TimerTriggerCause && !filteringSettings.isShowScheduledBuilds()) {
                return false;
            }
            if(cause instanceof Cause.UpstreamCause) {
                Cause.UpstreamCause upstreamCause = ((Cause.UpstreamCause) cause);
                List<Cause> upstreamCauses = upstreamCause.getUpstreamCauses();
                return isShowBuild(upstreamCauses, filteringSettings);
            }
        }
        return true;
    }

    private Run<?,?> getLastSuitableBuild() {
        if (filteringSettings.isDefaultSettings()) {
            return job.getLastBuild();
        }
        RunList builds =  job.getBuilds();

        for (Object build : builds) {
            Run<?,?> run = (Run<?,?>)build;
            List<Cause> causes = run.getCauses();
            if(isShowBuild(causes, filteringSettings)){
                return run;
            } else {
                continue;
            }
        }
        return null;
    }

    private BuildViewModel lastCompletedBuild() {
        BuildViewModel previousBuild = lastBuild();
        while (previousBuild.isRunning() && previousBuild.hasPreviousBuild()) {
            previousBuild = previousBuild.previousBuild();
        }

        return previousBuild;
    }

    private BuildViewModel buildViewOf(Run<?, ?> build) {
        if (null == build) {
            return new NullBuildView();
        }

        return BuildView.of(getLastSuitableBuild(), augmentor, relative, systemTime);
    }
}