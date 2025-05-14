package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.BuildMonitorView;

public class BuildMonitorViewPage extends JenkinsPage<BuildMonitorViewPage> {

    private final BuildMonitorView view;

    private BuildMonitorViewPage(Page page, BuildMonitorView view) {
        super(page, view.getAbsoluteUrl());
        this.view = view;
    }

    public static BuildMonitorViewPage from(Page page, BuildMonitorView view) {
        return new BuildMonitorViewPage(page, view);
    }

    @Override
    BuildMonitorViewPage waitForLoaded() {
        super.waitForLoaded();
        Locator grid = page.locator(".bm-grid");
        assertThat(grid).isVisible();
        return this;
    }

    public BuildMonitorViewPage hasJobsCount(int jobsCount) {
        Locator cells = page.locator(".bm-cell");
        assertThat(cells).hasCount(jobsCount);
        return this;
    }

    public JobComponent getJob(String jobName) {
        Locator job = page.locator(".bm-cell h2")
                .getByText(jobName)
                .locator("xpath=ancestor::*[contains(@class, 'bm-cell')]");
        return JobComponent.from(job);
    }

    public BuildMonitorViewPage hasJob(String jobName) {
        Locator link = page.locator(".bm-cell h2").getByText(jobName);
        assertThat(link).isVisible();
        return this;
    }

    public BuildMonitorViewPage hasNoJobs() {
        Locator heading = page.getByText("It seems a bit empty here...");
        Locator link = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Add some jobs"));
        assertThat(heading).isVisible();
        assertThat(link).isVisible();
        return this;
    }
}
