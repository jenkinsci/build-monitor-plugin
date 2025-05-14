package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.BuildMonitorViewUtils.createBuildMonitorView;
import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils.FreeStyleProjectUtils.createFreeStyleProject;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.config.PlaywrightConfig;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.pages.BuildMonitorViewPage;
import hudson.model.Result;
import hudson.tasks.Shell;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
@UsePlaywright(PlaywrightConfig.class)
class ShouldDescribeEachProjectTest {

    @Test
    void test(Page p, JenkinsRule j) {
        createFreeStyleProject(j, "Example Github Project")
                .addTask(new Shell(outputsGithubProjectLog()))
                //                    .addTask(new SetBuildDescriptionCommand(""))
                //            SetBuildDescription.to("Revision: \\1")
                //                                                    .basedOnLogLineMatching("Checking out Revision
                // ([\\w]{6})")),
                .run(Result.SUCCESS);
        var view = createBuildMonitorView(j, "Build Monitor").displayAllProjects();

        BuildMonitorViewPage.from(p, view)
                .goTo()
                .getJob("Example Github Project")
                .hasIdentifiedProblem("Revision: 67f4b3");
    }

    private String outputsGithubProjectLog() {
        var lines = List.of(
                "Started by an SCM change",
                "[EnvInject] - Loading node environment variables.",
                "Building remotely on 6479ff42 (lxc-fedora17 m1.small small) in workspace /scratch/jenkins/workspace/metricsd",
                "Cloning the remote Git repository",
                "Cloning repository git@github.com:jan-molak/metricsd.git",
                " > git init /scratch/jenkins/workspace/metricsd # timeout=10",
                "Fetching upstream changes from git@github.com:jan-molak/metricsd.git",
                " > git --version # timeout=10",
                " > git -c core.askpass=true fetch --tags --progress git@github.com:jan-molak/metricsd.git +refs/heads/*:refs/remotes/origin/*",
                " > git config remote.origin.url git@github.com:jan-molak/metricsd.git # timeout=10",
                " > git config --add remote.origin.fetch +refs/heads/*:refs/remotes/origin/* #timeout=10",
                " > git config remote.origin.url git@github.com:jan-molak/metricsd.git # timeout=10",
                "Fetching upstream changes from git@github.com:jan-molak/metricsd.git",
                " > git -c core.askpass=true fetch --tags --progress git@github.com:jan-molak/metricsd.git +refs/heads/*:refs/remotes/origin/*",
                " > git rev-parse refs/remotes/origin/master^{commit} # timeout=10",
                " > git rev-parse refs/remotes/origin/origin/master^{commit} # timeout=10",
                "Checking out Revision 67f4b364e14e4282115d8e518cfe75d86e9d3f2f (refs/remotes/origin/master)",
                " > git config core.sparsecheckout # timeout=10",
                " > git checkout -f 67f4b364e14e4282115d8e518cfe75d86e9d3f2f",
                " > git rev-list be0d664d303bfe3f66e58be1e3f3e723308f5108 # timeout=10");

        return Stream.of(lines).map(line -> String.format("echo \"%s\";", line)).collect(Collectors.joining());
    }
}
