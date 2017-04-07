package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.nameFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains methods used to filter the job name with specified prefix, suffix and regex
 */
public class NameFilterer {

    private String jobName;

    public NameFilterer(String jobName) {
        this.jobName = jobName;
    }

    public NameFilterer filterSuffix(String suffix) {
        if (suffix.length() <= 0) {
            return this;
        }

        if (jobName.substring(jobName.length() - suffix.length(), jobName.length()).equals(suffix.substring(0, suffix.length()))) {
            String filteredName = jobName.substring(0, jobName.length() - suffix.length());

            return new NameFilterer(filteredName);
        }

        return this;
    }


    public NameFilterer filterPrefix(String prefix) {
        if (prefix.length() <= 0) {
            return this;
        }

        if (jobName.substring(0, prefix.length()).equals(prefix.substring(0, prefix.length()))) {
            String filteredName = jobName.substring(prefix.length(), jobName.length());

            return new NameFilterer(filteredName);
        }

        return this;
    }

    public NameFilterer filterRegex(String regex) {
        if (regex.length() <= 0) {
            return this;
        }

        String filteredName;

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(jobName);
        filteredName = m.replaceAll("");

        return new NameFilterer(filteredName);
    }

    public String getJobName() {
        return jobName;
    }

}