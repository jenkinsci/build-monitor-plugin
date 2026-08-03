package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import hudson.model.FreeStyleProject;

public class FailureCauseManagementPage extends JenkinsPage<FailureCauseManagementPage> {

    private FailureCauseManagementPage(Page page, FreeStyleProject build) {
        super(page, build.getAbsoluteUrl() + "failure-cause-management/");
    }

    public static FailureCauseManagementPage from(Page page, FreeStyleProject build) {
        return new FailureCauseManagementPage(page, build);
    }

    @Override
    FailureCauseManagementPage waitForLoaded() {
        return this;
    }

    public FailureCauseManagementPage createFailureCause(String name, String matching, String pattern) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Create new"))
                .click();

        page.locator("input[name=\"_\\.name\"]").fill(name);
        page.locator("textarea[name=\"_\\.description\"]").fill(matching);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add Indication"))
                .click();

        page.getByRole(
                        AriaRole.BUTTON,
                        new Page.GetByRoleOptions()
                                .setName("Build Log Indication")
                                .setExact(true))
                .click();

        page.locator("input[name=\"pattern\"]").fill(pattern);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"))
                .click();

        return this;
    }
}
