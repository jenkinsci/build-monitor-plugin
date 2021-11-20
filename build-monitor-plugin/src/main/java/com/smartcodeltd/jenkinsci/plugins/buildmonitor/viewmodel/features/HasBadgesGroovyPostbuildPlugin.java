package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;

import org.jvnet.hudson.plugins.groovypostbuild.GroovyPostbuildAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Daniel Beland
 */
public class HasBadgesGroovyPostbuildPlugin implements Feature<HasBadgesGroovyPostbuildPlugin.Badges> {
    private ActionFilter filter = new ActionFilter();
    private JobView job;

    @Override
    public HasBadgesGroovyPostbuildPlugin of(JobView jobView) {
        this.job = jobView;

        return this;
    }

    @Override
    public Badges asJson() {
        Iterator<GroovyPostbuildAction> badges = job.lastBuild().allDetailsOf(GroovyPostbuildAction.class).stream().filter(filter).iterator();

        return badges.hasNext()
            ? new Badges(badges)
            : null; // `null` because we don't want to serialise an empty object
    }

    public static class Badges {
        private final List<Badge> badges = new ArrayList<>();

        public Badges(Iterator<GroovyPostbuildAction> badgeActions) {
            while (badgeActions.hasNext()) {
                badges.add(new Badge(badgeActions.next()));
            }
        }

        @JsonValue
        public List<Badge> value() {
            return Collections.unmodifiableList(new ArrayList<>(badges));
        }
    }

    public static class Badge {
        private final GroovyPostbuildAction badge;

        public Badge(GroovyPostbuildAction badge) {
            this.badge = badge;
        }

        @JsonProperty
        public final String text() {
            return badge.getText();
        }

        @JsonProperty
        public final String color() {
            return badge.getColor();
        }

        @JsonProperty
        public final String background() {
            return badge.getBackground();
        }

        @JsonProperty
        public final String border() {
            return badge.getBorder();
        }

        @JsonProperty
        public final String borderColor() {
            return badge.getBorderColor();
        }
    }

    private static class ActionFilter implements Predicate<GroovyPostbuildAction> {
        @Override
        public boolean test(GroovyPostbuildAction action) {
            return action.getIconPath() == null;
        }
    }
}
