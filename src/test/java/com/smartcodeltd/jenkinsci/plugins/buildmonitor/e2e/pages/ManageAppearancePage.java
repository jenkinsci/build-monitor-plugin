package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ManageAppearancePage extends JenkinsPage<ManageAppearancePage> {

    private final String baseUrl;

    public ManageAppearancePage(Page page, String baseUrl) {
        super(page, baseUrl + "manage/appearance/");
        this.baseUrl = baseUrl;
    }

    public ManageAppearancePage displayPipelineOnJobPage() {
        page.getByText("Show pipeline graph on job").click();
        return this;
    }

    public ManageAppearancePage displayPipelineOnBuildPage() {
        page.getByText("Show pipeline graph on build").click();
        return this;
    }

    public ManageAppearancePage setPipelineGraphAsConsoleProvider() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add"))
                .click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pipeline Graph View"))
                .click();
        return this;
    }

    public void save() {
        Locator button = page.getByRole(
                AriaRole.BUTTON, new Page.GetByRoleOptions().setExact(true).setName("Save"));
        assertThat(button).isEnabled();
        button.click();
        isAtUrl(this.baseUrl + "manage/");
    }
}
