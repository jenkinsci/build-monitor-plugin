package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claim;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.Claimed;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim.NotClaimed;
import hudson.model.Run;
import hudson.plugins.claim.ClaimBuildAction;

import java.util.HashSet;
import java.util.Set;

public class BuildAugmentor {

    private Set<Class<? extends Augmentation>> recognisedAugmentations = new HashSet<Class<? extends Augmentation>>();

    public Claim detailsOf(Run<?, ?> build, Class<Claim> augmentation) {
        // todo: remove the below naive implementation with something better once there is more plugins to support ...
        if (recognisedAugmentations.contains(augmentation)) {
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
