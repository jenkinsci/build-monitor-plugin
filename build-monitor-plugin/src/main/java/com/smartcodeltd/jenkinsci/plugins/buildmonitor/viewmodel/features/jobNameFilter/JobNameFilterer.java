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

    public JobNameFilterer filterSuffix(String suffix) {
        if (suffix.length() <= 0) {
            return this;
        }

        if (jobName.substring(jobName.length() - suffix.length(), jobName.length()).equals(suffix.substring(0, suffix.length()))) {
            String filteredName = jobName.substring(0, jobName.length() - suffix.length());

            return new JobNameFilterer(filteredName);
        }

        return this;
    }

    public JobNameFilterer filterPrefix(String prefix) {
        if (prefix.length() <= 0) {
            return this;
        }

        if (jobName.substring(0, prefix.length()).equals(prefix.substring(0, prefix.length()))) {
            String filteredName = jobName.substring(prefix.length(), jobName.length());

            return new JobNameFilterer(filteredName);
        }

        return this;
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