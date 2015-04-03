package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.Analysed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.Analysis;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.NotAnalysed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.NotClaimed;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import hudson.model.Run;
import hudson.plugins.claim.ClaimBuildAction;
import jenkins.model.Jenkins;

import java.util.HashSet;
import java.util.Set;

// The pattern around Augmentation is a dubious and quite Poltergeisty. It can probably be
// improved with something akin to a Visitor pattern
public class BuildAugmentor {

    public static String CLAIM_PLUGIN = "claim";
    public static String ANALYSER_PLUGIN = "build-failure-analyzer";

    public static BuildAugmentor fromDetectedPlugins() {
        BuildAugmentor augmentor = new BuildAugmentor();

        if (isInstalled(CLAIM_PLUGIN)) {
            augmentor.support(Claim.class);
        }
        if (isInstalled(ANALYSER_PLUGIN)) {
            augmentor.support(Analysis.class);
        }
        return augmentor;
    }

    private static boolean isInstalled(String plugin) {
        return Jenkins.getInstance().getPlugin(plugin) != null;
    }

    private Set<Class<? extends Augmentation>> recognisedAugmentations = new HashSet<Class<? extends Augmentation>>();

    @SuppressWarnings("unchecked")
    public <T extends Augmentation> T detailsOf(Run<?, ?> build, Class<T> augmentation) {
        if (augmentation.isAssignableFrom(Claim.class)) {
            return (T) detailsOfClaim(build);
        } else {
            assert augmentation.isAssignableFrom(Analysis.class) : "Unknown augmentation should never happen outside of development";
            return (T) detailsOfAnalysis(build);
        }
    }

    private <T extends Augmentation> boolean isSupported(Class<T> augmentation) {
        return recognisedAugmentations.contains(augmentation);
    }

    private Claim detailsOfClaim(Run<?, ?> build) {
        if (isSupported(Claim.class)) {
            ClaimBuildAction action = build.getAction(ClaimBuildAction.class);

            if (action != null) {
                return new Claimed(action);
            }
        }
        return new NotClaimed();
    }

    private Analysis detailsOfAnalysis(Run<?, ?> build) {
        if (isSupported(Analysis.class)) {
            FailureCauseBuildAction action = build.getAction(FailureCauseBuildAction.class);

            if (action != null) {
                // Should we also check if failures were found? is it "Analysed" or "HasKnownFailures"
                return new Analysed(action);
            }
        }
        return new NotAnalysed();
    }

    public void support(Class<? extends Augmentation> typeOfAugmentation) {
        recognisedAugmentations.add(typeOfAugmentation);
    }
}