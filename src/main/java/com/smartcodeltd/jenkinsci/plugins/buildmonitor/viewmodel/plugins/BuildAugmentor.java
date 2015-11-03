package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins;

import hudson.model.Hudson;
import hudson.model.AbstractProject;
import hudson.model.DependencyGraph;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.Analysed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.Analysis;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.bfa.NotAnalysed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.NotClaimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob.Multijob;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob.InMultijob;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.multijob.NoMultijob;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCauseBuildAction;
import hudson.model.Run;
import hudson.plugins.claim.ClaimBuildAction;
import com.tikal.jenkins.plugins.multijob.MultiJobProject;
import jenkins.model.Jenkins;

import java.util.HashSet;
import java.util.Set;

// The pattern around Augmentation is a dubious and quite Poltergeisty. It can probably be
// improved with something akin to a Visitor pattern
public class BuildAugmentor {

    public static String CLAIM_PLUGIN = "claim";
    public static String ANALYSER_PLUGIN = "build-failure-analyzer";
    public static String MULTIJOB_PLUGIN = "jenkins-multijob-plugin";

    public static BuildAugmentor fromDetectedPlugins() {
        BuildAugmentor augmentor = new BuildAugmentor();

        if (isInstalled(CLAIM_PLUGIN)) {
            augmentor.support(Claim.class);
        }
        if (isInstalled(ANALYSER_PLUGIN)) {
            augmentor.support(Analysis.class);
        }
        if (isInstalled(MULTIJOB_PLUGIN)) {
            augmentor.support(Multijob.class);
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
        } else if (augmentation.isAssignableFrom(Multijob.class)) {
            return (T) detailsOfMultijob(build);
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

    private Multijob detailsOfMultijob(Run<?, ?> build) {
        if (isSupported(Multijob.class)) {
            AbstractProject<?, ?> project = ((AbstractProject) build.getParent());
            DependencyGraph depsgraph = Hudson.getInstance().getDependencyGraph();
            MultiJobProject parent = null;
            for (final AbstractProject<?, ?> p : depsgraph.getUpstream(project)) {
                if (p instanceof MultiJobProject) {
                    parent = (MultiJobProject) p;
                    break;
                }
            }

            if (parent != null) {
                return new InMultijob(project, parent);
            }
        }
        return new NoMultijob();
    }

    public void support(Class<? extends Augmentation> typeOfAugmentation) {
        recognisedAugmentations.add(typeOfAugmentation);
    }
}
