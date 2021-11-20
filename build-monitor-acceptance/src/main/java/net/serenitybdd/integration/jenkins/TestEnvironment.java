package net.serenitybdd.integration.jenkins;

import net.serenitybdd.integration.jenkins.environment.rules.ApplicativeTestRule;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class TestEnvironment {
    private final JenkinsInstance instance;
    private final List<ApplicativeTestRule<JenkinsInstance>> rulesToBeAppliedBeforeJenkinsStart = new ArrayList<>();
    private final List<ApplicativeTestRule<JenkinsInstance>> rulesToBeAppliedAfterJenkinsStart  = new ArrayList<>();

    public TestEnvironment(JenkinsInstance instance) {
        this.instance = instance;
    }


    public JenkinsInstance create() {
        return instance.beforeStartApply(rulesToBeAppliedBeforeJenkinsStart).afterStartApply(rulesToBeAppliedAfterJenkinsStart);
    }

    public <ATR extends ApplicativeTestRule<JenkinsInstance>> TestEnvironment beforeStart(ATR... rules) {
        return beforeStart(asList(rules));
    }

    public <ATR extends ApplicativeTestRule<JenkinsInstance>> TestEnvironment afterStart(ATR... rules) {
        return afterStart(asList(rules));
    }

    public <ATR extends ApplicativeTestRule<JenkinsInstance>> TestEnvironment beforeStart(List<ATR> rules) {
        this.rulesToBeAppliedBeforeJenkinsStart.addAll(rules);

        return this;
    }

    public <ATR extends ApplicativeTestRule<JenkinsInstance>> TestEnvironment afterStart(List<ATR> rules) {
        this.rulesToBeAppliedAfterJenkinsStart.addAll(rules);

        return this;
    }
}
