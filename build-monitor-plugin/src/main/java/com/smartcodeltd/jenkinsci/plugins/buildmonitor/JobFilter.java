package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.google.common.base.Strings;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Job;
import hudson.model.TopLevelItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static hudson.Util.filter;

public class JobFilter {
    private final Pattern includePattern;
    private final Pattern excludePattern;
    private Class<? extends ItemGroup> abstractFolderClass;

    public JobFilter(Config config) {
        String include = config.getBranchesToInclude();
        String exclude = config.getBranchesToExclude();
        includePattern = Strings.isNullOrEmpty(include) ? null : Pattern.compile(include);
        excludePattern = Strings.isNullOrEmpty(exclude) ? null : Pattern.compile(exclude);
        try {
            abstractFolderClass = Class.forName("com.cloudbees.hudson.plugins.folder.AbstractFolder")
                    .asSubclass(ItemGroup.class);
        } catch (ClassNotFoundException e) {
            abstractFolderClass = null;
        }
    }

    public List<Job<?, ?>> filterJobs(Collection<? extends Item> items) {
        List<Job<?, ?>> jobs = new ArrayList<Job<?, ?>>();

        if (items == null) {
            return jobs;
        }

        if (abstractFolderClass != null) {
            for (ItemGroup<?> folder : filter(items, abstractFolderClass)) {
                Collection<?> folderItems = folder.getItems();
                if (folderItems == null) {
                    continue;
                }
                List<Job> groupJobs = filter(folderItems, Job.class);
                for (Job job : groupJobs) {
                    String relativename = job.getRelativeNameFrom(folder);
                    boolean shouldInclude = includePattern == null || includePattern.matcher(relativename).find();
                    boolean shouldExclude = excludePattern != null && excludePattern.matcher(relativename).find();
                    if (shouldInclude && !shouldExclude) {
                        jobs.add(job);
                    }
                }
            }
        }

        for (Job job : filter(items, Job.class)) {
            jobs.add(job);
        }

        return jobs;
    }

}
