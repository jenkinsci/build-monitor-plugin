package com.smartcodeltd.jenkinsci.plugins.buildmonitor.e2e.utils;

import com.sonyericsson.jenkins.plugins.bfa.PluginImpl;
import com.sonyericsson.jenkins.plugins.bfa.db.LocalFileKnowledgeBase;
import com.sonyericsson.jenkins.plugins.bfa.model.FailureCause;
import com.sonyericsson.jenkins.plugins.bfa.model.indication.BuildLogIndication;
import org.jvnet.hudson.test.JenkinsRule;

public class FailureCauseManagementUtils {

    private static final String BUILD_LOG_PATTERN = "Build step 'Execute shell' marked build as failure";

    public static void createFailureCause(JenkinsRule j, String title, String description) {
        try {
            PluginImpl plugin = PluginImpl.getInstance();
            LocalFileKnowledgeBase knowledgeBase = (LocalFileKnowledgeBase) plugin.getKnowledgeBase();

            // Create the failure cause and indication
            FailureCause newCause = new FailureCause(title, description);
            newCause.addIndication(new BuildLogIndication(BUILD_LOG_PATTERN));

            // Save back to knowledge base
            knowledgeBase.saveCause(newCause);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create failure cause", e);
        }
    }
}
