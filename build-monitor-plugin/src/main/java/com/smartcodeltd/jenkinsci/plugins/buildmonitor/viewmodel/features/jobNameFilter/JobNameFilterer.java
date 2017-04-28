package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.jobNameFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains methods used to filter the job name with specified prefix, suffix and regex
 */
public class JobNameFilterer {
    private String jobName;

    public JobNameFilterer(String jobName) {
        this.jobName = jobName;
    }

    public JobNameFilterer filterRegex(String regex) {
        if (regex.length() <= 0) {
            return this;
        }

        String filteredName;

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(jobName);
        filteredName = m.replaceAll("");

        return new JobNameFilterer(filteredName);
    }

    public String getJobName() {
        return jobName;
    }
}