package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.build.GetBuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.build.GetLastBuild;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.order.BaseOrder;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.order.ByName;
import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Job;
import java.util.Comparator;
import java.util.Optional;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

public class Config implements Describable<Config> {

    @Extension
    public static class ConfigDescriptor extends Descriptor<Config> {}

    private Boolean colourBlindMode;
    private Boolean displayCommitters;
    private Integer maxColumns;
    private Double textScale;
    private Boolean showBadges;
    private DisplayOptions displayBadges;
    private GetBuildViewModel displayBadgesFrom;
    private BuildFailureAnalyzerDisplayedField buildFailureAnalyzerDisplayedField;
    private Boolean displayJUnitProgress;

    public static Config defaultConfig() {
        return new Config();
    }

    @Override
    public Descriptor<Config> getDescriptor() {
        return Jenkins.get().getDescriptor(this.getClass());
    }

    @DataBoundConstructor
    public Config() {}

    public Comparator<Job<?, ?>> getOrder() {
        /*
         * Jenkins unmarshals objects from config.xml by setting their private fields directly and without invoking their constructors.
         * In order to retrieve a potentially already persisted field try to first get the field, if that didn't work - use defaults.
         *
         * This is defensive coding to avoid issues such as this one:
         *  https://github.com/jenkinsci/build-monitor-plugin/issues/43
         */

        return Optional.ofNullable(order).orElse(new ByName());
    }

    public void setOrder(BaseOrder order) {
        this.order = order;
    }

    public BuildFailureAnalyzerDisplayedField getBuildFailureAnalyzerDisplayedField() {
        return Optional.ofNullable(buildFailureAnalyzerDisplayedField).orElse(BuildFailureAnalyzerDisplayedField.Name);
    }

    public void setBuildFailureAnalyzerDisplayedField(String buildFailureAnalyzerDisplayedField) {
        this.buildFailureAnalyzerDisplayedField =
                BuildFailureAnalyzerDisplayedField.valueOf(buildFailureAnalyzerDisplayedField);
    }

    public boolean getColourBlindMode() {
        return Optional.ofNullable(colourBlindMode).orElse(false);
    }

    public void setColourBlindMode(boolean flag) {
        this.colourBlindMode = flag;
    }

    public boolean getDisplayCommitters() {
        return Optional.ofNullable(displayCommitters).orElse(true);
    }

    public void setDisplayCommitters(boolean flag) {
        this.displayCommitters = flag;
    }

    public boolean getShowBadges() {
        return Optional.ofNullable(showBadges).orElse(true);
    }

    public void setShowBadges(boolean flag) {
        this.showBadges = flag;
    }

    public DisplayOptions getDisplayBadges() {
        return Optional.ofNullable(displayBadges).orElse(DisplayOptions.UserSetting);
    }

    public void setDisplayBadges(String option) {
        this.displayBadges = DisplayOptions.valueOf(option);
    }

    public GetBuildViewModel getDisplayBadgesFrom() {
        return Optional.ofNullable(displayBadgesFrom).orElse(new GetLastBuild());
    }

    public void setDisplayBadgesFrom(GetBuildViewModel displayBadgesFrom) {
        this.displayBadgesFrom = displayBadgesFrom;
    }

    public boolean getDisplayJUnitProgress() {
        return Optional.ofNullable(displayJUnitProgress).orElse(true);
    }

    public void setDisplayJUnitProgress(boolean flag) {
        this.displayJUnitProgress = flag;
    }

    public int getMaxColumns() {
        return Optional.ofNullable(maxColumns).orElse(2);
    }

    public void setMaxColumns(int maxColumns) {
        this.maxColumns = maxColumns;
    }

    public double getTextScale() {
        return Optional.ofNullable(textScale).orElse(1.0);
    }

    public void setTextScale(double scale) {
        this.textScale = scale;
    }

    @Override
    public String toString() {
        return String.format("Config{order=%s}", order.getClass().getSimpleName());
    }

    // --

    public enum BuildFailureAnalyzerDisplayedField {
        Name("name"),
        Description("description"),
        None("none");

        private final String value;

        BuildFailureAnalyzerDisplayedField(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum DisplayOptions {
        Always,
        Never,
        UserSetting
    }

    private BaseOrder order;
}
