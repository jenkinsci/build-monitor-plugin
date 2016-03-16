package com.smartcodeltd.jenkinsci.plugins.build_monitor.matchers;

import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectInformation;
import com.smartcodeltd.jenkinsci.plugins.build_monitor.model.ProjectStatus;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static java.lang.String.format;

public class ProjectInformationMatchers {
    public static ProjectStatusMatcher displaysProjectStatusAs(ProjectStatus desiredStatus) {
        return new ProjectStatusMatcher(desiredStatus);
    }

    public static class ProjectStatusMatcher extends TypeSafeMatcher<ProjectInformation> {

        private final ProjectStatus desiredStatus;

        public ProjectStatusMatcher(ProjectStatus desiredStatus) {
            this.desiredStatus = desiredStatus;
        }

        @Override
        protected boolean matchesSafely(ProjectInformation information) {
            return information.status().contains(desiredStatus);
        }

        @Override
        public void describeTo(Description description) {
            description
                    .appendText("displaying the status as ")
                    .appendText(format("'%s'", desiredStatus.toString()));
        }
    }
}
