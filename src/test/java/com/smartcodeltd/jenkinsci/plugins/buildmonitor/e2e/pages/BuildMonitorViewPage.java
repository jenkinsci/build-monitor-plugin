package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils;
import java.util.List;

public class BuildMonitorViewPage extends JenkinsPage<BuildMonitorViewPage> {

    private BuildMonitorViewPage(Page page, BuildMonitorViewUtils.FluentBuildMonitorView view) {
        super(page, view.get().getAbsoluteUrl());
    }

    public static BuildMonitorViewPage from(Page page, BuildMonitorViewUtils.FluentBuildMonitorView view) {
        return new BuildMonitorViewPage(page, view);
    }

    @Override
    BuildMonitorViewPage waitForLoaded() {
        super.waitForLoaded();
        Locator content = page.locator(".bm-grid-viewport, .jenkins-notice").first();
        assertThat(content).isVisible();
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

    public BuildMonitorViewPage hasPageCount(int pageCount) {
        Locator pages = page.locator(".bm-grid-page");
        assertThat(pages).hasCount(pageCount);
        return this;
    }

    public BuildMonitorViewPage scrollPastPageBoundaryTowards(int pageNumber) {
        int pageIndex = pageNumber - 1;
        Locator viewport = page.locator(".bm-grid-viewport");
        viewport.evaluate(
                "(element, pageIndex) => element.scrollTo({ left: element.clientWidth * pageIndex + 120 })", pageIndex);
        return this;
    }

    public BuildMonitorViewPage hasSnappedToPage(int pageNumber) {
        int pageIndex = pageNumber - 1;

        page.waitForFunction(
                "([selector, pageIndex]) => {"
                        + "  const element = document.querySelector(selector);"
                        + "  return !!element && Math.abs(element.scrollLeft - (element.clientWidth * pageIndex)) < 4;"
                        + "}",
                List.of(".bm-grid-viewport", pageIndex));

        return this;
    }

    public BuildMonitorViewPage hasActivePage(int pageNumber) {
        Locator dot = page.locator(".bm-grid-pagination__dot").nth(pageNumber - 1);
        assertThat(dot).hasAttribute("aria-current", "page");
        return this;
    }
}
