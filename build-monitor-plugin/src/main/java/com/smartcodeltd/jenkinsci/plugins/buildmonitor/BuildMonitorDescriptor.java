package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import hudson.Util;
import hudson.model.ViewDescriptor;
import hudson.util.FormValidation;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest2;

public final class BuildMonitorDescriptor extends ViewDescriptor {

    public BuildMonitorDescriptor() {
        super(BuildMonitorView.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Build Monitor View";
    }

    // Copy-n-paste from ListView$Descriptor as sadly we cannot inherit from that class
    @SuppressWarnings({"lgtm[jenkins/csrf]", "lgtm[jenkins/no-permission-check]"})
    public FormValidation doCheckIncludeRegex(@QueryParameter String value) {
        String v = Util.fixEmpty(value);
        if (v != null) {
            try {
                Pattern.compile(v);
            } catch (PatternSyntaxException pse) {
                return FormValidation.error(pse.getMessage());
            }
        }
        return FormValidation.ok();
    }

    @Override
    public boolean configure(StaplerRequest2 req, JSONObject json) {
        req.bindJSON(this, json.getJSONObject("build-monitor"));
        save();

        return true;
    }

    private boolean permissionToCollectAnonymousUsageStatistics = true;

    public boolean getPermissionToCollectAnonymousUsageStatistics() {
        return this.permissionToCollectAnonymousUsageStatistics;
    }

    @SuppressWarnings("unused") // used in global.jelly
    public void setPermissionToCollectAnonymousUsageStatistics(boolean collect) {
        this.permissionToCollectAnonymousUsageStatistics = collect;
    }

    @SuppressWarnings({"lgtm[jenkins/csrf]", "lgtm[jenkins/no-permission-check]"})
    public FormValidation doCheckMaxColumns(@QueryParameter String value) {
        String v = Util.fixEmpty(value);
        if (v != null) {
            try {
                int intValue = Integer.parseInt(v);
                if (intValue > 0) {
                    return FormValidation.ok();
                } else {
                    return FormValidation.error("Must be an integer, greater than 0.");
                }
            } catch (NumberFormatException e) {
                return FormValidation.error("Must be an integer.");
            }
        }
        return FormValidation.ok();
    }

    @SuppressWarnings({"lgtm[jenkins/csrf]", "lgtm[jenkins/no-permission-check]"})
    public FormValidation doCheckTextScale(@QueryParameter String value) {
        String v = Util.fixEmpty(value);
        if (v != null) {
            try {
                double doubleValue = Double.parseDouble(v);
                if (doubleValue > 0.0) {
                    return FormValidation.ok();
                } else {
                    return FormValidation.error("Must be a double, greater than 0.0.");
                }
            } catch (NumberFormatException e) {
                return FormValidation.error("Must be a double.");
            }
        }
        return FormValidation.ok();
    }
}
