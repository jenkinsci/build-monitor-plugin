package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class PipelineBuildPage extends JenkinsPage<PipelineBuildPage> {
    private final String buildName;

    public PipelineBuildPage(Page page, String pageUrl, String buildName) {
        super(page, pageUrl);
        this.buildName = buildName;
    }

    @Override
    PipelineBuildPage waitForLoaded() {
        super.waitForLoaded();
        Locator heading = page.getByRole(
                AriaRole.HEADING, new Page.GetByRoleOptions().setLevel(1).setName(buildName));
        assertThat(heading).isVisible();
        assertThat(getPipelineSection()).isVisible();
        return this;
    }

    private Locator getPipelineSection() {
        return page.locator("#graph").locator(".PWGx-PipelineGraph-container");
    }
}
