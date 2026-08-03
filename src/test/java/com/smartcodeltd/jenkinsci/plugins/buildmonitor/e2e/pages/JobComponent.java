package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import hudson.model.Result;

public class JobComponent {

    private final Locator component;

    public JobComponent(Locator component) {
        this.component = component;
    }

    public static JobComponent from(Locator component) {
        return new JobComponent(component);
    }

    public JobComponent hasStatus(Result result) {
        String expectedClass;

        if (result == Result.SUCCESS) {
            expectedClass = "bm-cell--success";
        } else if (result == Result.FAILURE) {
            expectedClass = "bm-cell--failing";
        } else {
            throw new IllegalArgumentException("Unknown Result: " + result);
        }

        String classAttr = component.getAttribute("class");

        if (classAttr == null || !classAttr.contains(expectedClass)) {
            throw new AssertionError("Expected status class '" + expectedClass + "' not found in: " + classAttr);
        }

        return this;
    }

    public JobComponent hasDescription(String badgeName) {
        Locator description = component.getByText(badgeName);
        assertThat(description).isVisible();
        return this;
    }

    public JobComponent hasIdentifiedProblem(String identifiedProblem) {
        assertThat(component).containsText(identifiedProblem);
        return this;
    }

    public JobComponent hasBadge(String badgeName) {
        Locator badge = component.locator(".bm-badge").getByText(badgeName);
        assertThat(badge).isVisible();
        return this;
    }

    public JobComponent hasBuilds(String... builds) {
        for (String build : builds) {
            assertThat(component).containsText(build, new LocatorAssertions.ContainsTextOptions().setTimeout(10000));
        }
        return this;
    }

    public JobComponent hasStage(String stage) {
        assertThat(component).containsText(stage);
        return this;
    }
}
