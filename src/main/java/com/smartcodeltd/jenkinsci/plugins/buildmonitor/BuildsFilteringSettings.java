package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

/**
 * Created by okotovic on 25.12.2014.
 */
public class BuildsFilteringSettings {
    private String[] usernames;
    private boolean showScheduledBuilds;
    private boolean showAnonymousBuilds;

    public BuildsFilteringSettings(String[] usernames, boolean showScheduledBuilds, boolean showAnonymousBuilds) {
        this.usernames = usernames;
        this.showScheduledBuilds = showScheduledBuilds;
        this.showAnonymousBuilds = showAnonymousBuilds;
    }

    public BuildsFilteringSettings(){
        this.usernames  = new String[0];
        this.showScheduledBuilds = true;
        this.showAnonymousBuilds = true;
    }

    public boolean isDefaultSettings() {
        return (this.usernames.length == 0 && this.showScheduledBuilds && this.showAnonymousBuilds);
    }

    public String[] getUsernames() {
        return usernames;
    }

    public void setUsernames(String[] usernames) {
        for (int i = 0; i < usernames.length; i++)
            usernames[i] = usernames[i].trim();
        this.usernames = usernames;
    }

    public boolean isShowScheduledBuilds() {
        return showScheduledBuilds;
    }

    public void setShowScheduledBuilds(boolean showScheduledBuilds) {
        this.showScheduledBuilds = showScheduledBuilds;
    }

    public boolean isShowAnonymousBuilds() {
        return showAnonymousBuilds;
    }

    public void setShowAnonymousBuilds(boolean showAnonymousBuilds) {
        this.showAnonymousBuilds = showAnonymousBuilds;
    }
}
