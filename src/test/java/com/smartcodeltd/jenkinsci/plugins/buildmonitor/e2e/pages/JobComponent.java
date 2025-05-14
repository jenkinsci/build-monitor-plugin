package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
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

    public JobComponent hasBadge(String badgeName) {
        Locator badge = component.locator(".bm-badge").getByText(badgeName);
        assertThat(badge).isVisible();
        return this;
    }

    public JobComponent hasTestProgressBars() {
        Locator badge = component.locator(".bs-progress");
        assertThat(badge).isVisible();
        return this;
    }
}
