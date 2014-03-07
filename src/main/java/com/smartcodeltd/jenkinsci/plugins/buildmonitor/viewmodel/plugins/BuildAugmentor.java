package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.NotClaimed;
import hudson.model.Hudson;
import hudson.model.Run;
import hudson.plugins.claim.ClaimBuildAction;

import java.util.HashSet;
import java.util.Set;

// The pattern around Augmentation is a dubious and quite Poltergeisty. It can probably be
// improved with something akin to a Visitor pattern
public class BuildAugmentor {

    public static String CLAIM_PLUGIN = "claim";

    public static BuildAugmentor fromDetectedPlugins() {
        BuildAugmentor augmentor = new BuildAugmentor();

        if (isInstalled(CLAIM_PLUGIN)) {
            augmentor.support(Claim.class);
        }
        return augmentor;
    }

    private static boolean isInstalled(String plugin) {
        return Hudson.getInstance().getPlugin(plugin) != null;
    }


    private Set<Class<? extends Augmentation>> recognisedAugmentations = new HashSet<Class<? extends Augmentation>>();

    @SuppressWarnings("unchecked")
    public <T extends Augmentation> T detailsOf(Run<?, ?> build, Class<T> augmentation) {
        //if (augmentation.isAssignableFrom(Claim.class)) {
        return (T) detailsOf(build);
        //}
    }

    private <T extends Augmentation> boolean isSupported(Class<T> augmentation) {
        return recognisedAugmentations.contains(augmentation);
    }

    private Claim detailsOf(Run<?, ?> build) {
        if (isSupported(Claim.class)) {
            ClaimBuildAction action = build.getAction(ClaimBuildAction.class);

            if (action != null) {
                return new Claimed(action);
            }
        }
        return new NotClaimed();
    }

    public void support(Class<? extends Augmentation> typeOfAugmentation) {
        recognisedAugmentations.add(typeOfAugmentation);
    }

}
