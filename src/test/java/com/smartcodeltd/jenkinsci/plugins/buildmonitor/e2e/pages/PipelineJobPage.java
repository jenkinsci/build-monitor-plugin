package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.net.URI;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineJobPage extends JenkinsPage<PipelineJobPage> {

    private static final Logger log = LoggerFactory.getLogger(PipelineJobPage.class);
    private final String jobName;

    public PipelineJobPage(Page page, WorkflowJob job) {
        super(page, job.getAbsoluteUrl());
        jobName = job.getDisplayName();
    }

    @Override
    PipelineJobPage waitForLoaded() {
        super.waitForLoaded();
        Locator heading = page.getByRole(
                AriaRole.HEADING, new Page.GetByRoleOptions().setName(jobName).setLevel(1));
        assertThat(heading).isVisible();
        assertThat(getPipelineSection()).isVisible();
        return this;
    }

    public PipelineJobPage hasBuilds(int count) {
        log.info("Checking that {} builds are visible for the job {}", count, jobName);
        assertThat(getPipelineSection().locator(".pgv-single-run")).hasCount(count);
        log.info("Verified {} builds are visible", count);
        return this;
    }

    private Locator getPipelineSection() {
        return page.locator("#multiple-pipeline-root");
    }

    public PipelineBuild nthBuild(int index) {
        log.info("Getting the {} build for the job {}", index, jobName);
        Locator build = getPipelineSection().locator(".pgv-single-run").nth(index);
        assertThat(build).isVisible();

        return new PipelineBuild(this, build);
    }

    public static class PipelineBuild {

        private final PipelineJobPage parent;
        private final Locator wrapper;

        PipelineBuild(PipelineJobPage parent, Locator wrapper) {
            this.parent = parent;
            this.wrapper = wrapper;
        }

        public PipelineBuildPage goToBuild() {
            Locator link = wrapper.getByRole(AriaRole.LINK).nth(0);
            String relativeBuildUrl = link.getAttribute("href");
            String text = link.textContent();
            String buildName = text.replace(link.locator("span").textContent(), "") // remove timing info
                    .replace(link.getByRole(AriaRole.IMG).textContent(), "") // remove svg title
                    .trim();

            link.click();

            String pageUrl =
                    URI.create(parent.pageUrl).resolve(relativeBuildUrl).toString();

            pageUrl = pageUrl.endsWith("/") ? pageUrl : pageUrl + "/";

            return new PipelineBuildPage(parent.page, pageUrl, buildName).waitForLoaded();
        }
    }
}
