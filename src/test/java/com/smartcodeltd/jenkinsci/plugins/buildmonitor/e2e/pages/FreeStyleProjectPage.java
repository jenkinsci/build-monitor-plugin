package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hudson.model.FreeStyleProject;

public class FreeStyleProjectPage extends JenkinsPage<FreeStyleProjectPage> {

    private final FreeStyleProject project;

    public FreeStyleProjectPage(Page page, FreeStyleProject project) {
        super(page, project.getAbsoluteUrl());
        this.project = project;
    }

    @Override
    FreeStyleProjectPage waitForLoaded() {
        super.waitForLoaded();
        Locator heading = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(project.getDisplayName()).setLevel(1));
        assertThat(heading).isVisible();
        return this;
    }
}
