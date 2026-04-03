/*
 * The MIT License
 *
 * Copyright (c) 2013-2015, Jan Molak, SmartCode Ltd http://smartcodeltd.co.uk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.api.Respond;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.build.GetBuildViewModel;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.facade.StaticJenkinsAPIs;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.order.BaseOrder;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobViews;
import hudson.Extension;
import hudson.model.Descriptor.FormException;
import hudson.model.ItemGroup;
import hudson.model.Job;
import hudson.model.ListView;
import hudson.model.TopLevelItem;
import hudson.model.View;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.Comparator;
import java.util.List;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.WebMethod;

/**
 * @author Jan Molak
 */
public class BuildMonitorView extends ListView {
    private static final Logger logger = Logger.getLogger(BuildMonitorView.class.getName());
    @Extension
    public static final BuildMonitorDescriptor descriptor = new BuildMonitorDescriptor();

    private String title;

    /**
     * @param name  Name of the view to be displayed on the Views tab
     * @param title Title to be displayed on the Build Monitor; defaults to 'name' if not set
     */
    @DataBoundConstructor
    public BuildMonitorView(String name, String title) {
        super(name);

        this.title = title;
    }

    @SuppressWarnings("unused") // used in .jelly
    public String getTitle() {
        return isGiven(title) ? title : getDisplayName();
    }

    @SuppressWarnings("unused") // used in .jelly
    public boolean isEmpty() {
        return jobViews().isEmpty();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public String currentOrder() {
        return currentConfig().getOrder().getClass().getSimpleName();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public String currentBuildFailureAnalyzerDisplayedField() {
        return currentConfig().getBuildFailureAnalyzerDisplayedField().getValue();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public boolean isDisplayCommitters() {
        return currentConfig().getDisplayCommitters();
    }

    // used in the configure-entries.jelly and main-settings.jelly forms
    @SuppressWarnings("unused")
    public double getTextScale() {
        return currentConfig().getTextScale();
    }

    // used in the configure-entries.jelly and main-settings.jelly forms
    @SuppressWarnings("unused")
    public int getMaxColumns() {
        return currentConfig().getMaxColumns();
    }

    // used in the configure-entries.jelly and main-settings.jelly forms
    @SuppressWarnings("unused")
    public boolean isColourBlindMode() {
        return currentConfig().getColourBlindMode();
    }

    // used in the configure-entries.jelly and main-settings.jelly forms
    @SuppressWarnings("unused")
    public boolean isShowBadges() {
        return currentConfig().getShowBadges();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public String currentDisplayBadges() {
        return currentConfig().getDisplayBadges().name();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public String currentDisplayBadgesFrom() {
        return currentConfig().getDisplayBadgesFrom().getClass().getSimpleName();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public boolean isDisplayJUnitProgress() {
        return currentConfig().getDisplayJUnitProgress();
    }

    @SuppressWarnings("unused") // used in the configure-entries.jelly form
    public int getAutoRefreshEvery() {
        return currentConfig().getAutoRefreshEvery();
    }

    @Override
    protected void initColumns() {}

    @DataBoundSetter
    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return currentConfig();
    }

    @Override
    protected void submit(StaplerRequest2 req) throws ServletException, IOException, FormException {
        super.submit(req);

        JSONObject json = req.getSubmittedForm();

        synchronized (this) {
            String requestedOrdering = req.getParameter("order");
            String displayBadgesFrom = req.getParameter("displayBadgesFrom");
            title = req.getParameter("title");
            String maxColumns = req.getParameter("maxColumns");
            String textScale = req.getParameter("textScale");

            currentConfig().setColourBlindMode(json.optBoolean("colourBlindMode", false));
            currentConfig().setShowBadges(json.optBoolean("showBadges", true));
            currentConfig().setDisplayBadges(req.getParameter("displayBadges"));
            currentConfig().setDisplayCommitters(json.optBoolean("displayCommitters", true));
            currentConfig()
                    .setBuildFailureAnalyzerDisplayedField(req.getParameter("buildFailureAnalyzerDisplayedField"));
            currentConfig().setDisplayJUnitProgress(json.optBoolean("displayJUnitProgress", true));
            currentConfig().setAutoRefreshEvery(json.optInt("autoRefreshEvery", 4));

            try {
                currentConfig().setOrder(orderIn(requestedOrdering));
            } catch (Exception e) {
                throw new FormException("Can't order projects by " + requestedOrdering, "order");
            }

            try {
                currentConfig().setMaxColumns(Integer.parseInt(maxColumns));
            } catch (Exception e) {
                throw new FormException(
                        "Invalid value of 'Maximum number of columns': '" + maxColumns + "' (should be double).",
                        maxColumns);
            }

            try {
                currentConfig().setTextScale(Double.parseDouble(textScale));
            } catch (Exception e) {
                throw new FormException(
                        "Invalid value of 'Text scale': '" + textScale + "' (should be double).", textScale);
            }

            try {
                currentConfig().setDisplayBadgesFrom(getBuildViewModelIn(displayBadgesFrom));
            } catch (Exception e) {
                throw new FormException("Can't display badges from " + displayBadgesFrom, "displayBadgesFrom");
            }
        }
    }

    /**
     * Because of how org.kohsuke.stapler.HttpResponseRenderer is implemented
     * it can only work with net.sf.JSONObject in order to produce correct application/json output
     *
     * @return Json representation of JobViews
     */
    @WebMethod(name = "fetchJobViews")
    public JSONObject fetchJobViews() throws Exception {
        return Respond.withSuccess(jobViews());
    }

    // --
    private boolean isGiven(String value) {
        return !(value == null || "".equals(value));
    }

    private List<JobView> jobViews() {
        JobViews views = new JobViews(new StaticJenkinsAPIs(), currentConfig());

        List<Job<?, ?>> projects = getJobsToDisplay();
        List<JobView> jobs = new ArrayList<>();

        projects.sort(currentConfig().getOrder());

        for (Job<?, ?> project : projects) {
            jobs.add(views.viewOf(project));
        }

        return jobs;
    }

    /**
     * Returns the list of jobs to be displayed in the view, with multibranch pipeline projects
     * expanded into their constituent branch jobs. This ensures that branches from multibranch
     * pipelines are shown even when recursion is disabled, and that the multibranch project
     * itself (which typically has no builds) is not displayed.
     */
    private List<Job<?, ?>> getJobsToDisplay() {
        List<Job<?, ?>> processedJobs = new ArrayList<>();
        boolean recurse = isRecurse();

        for (TopLevelItem item : super.getItems()) {
            if (isMultibranchPipeline(item)) {
                if (recurse) {
                    // When recursion is enabled, children are already discovered by Jenkins.
                    // Skip the multibranch container to avoid showing it with no builds.
                    continue;
                }
                // Expand multibranch into branch jobs
                expandIfItemGroup(item, processedJobs);
            } else if (item instanceof Job<?, ?> job) {
                processedJobs.add(job);
            }
        }

        return processedJobs;
    }

    private void expandIfItemGroup(Object item, List<Job<?, ?>> target) {
        if (item instanceof ItemGroup<?> group) {
            Collection<? extends TopLevelItem> children = group.getItems();
            if (children != null) {
                for (TopLevelItem child : children) {
                    if (child instanceof Job<?, ?> childJob) {
                        target.add(childJob);
                    }
                }
            }
        }
    }

    /**
     * Checks whether the given job is a multibranch pipeline project.
     * Uses reflection to avoid a hard dependency on the workflow-multibranch plugin.
     * The Class.forName result is cached for performance.
     */
    private static Class<?> cachedMbpClass = null;
    private static boolean mbpClassResolved = false;

    private boolean isMultibranchPipeline(Object item) {
        if (!mbpClassResolved) {
            try {
                cachedMbpClass = Class.forName("org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject");
            } catch (ClassNotFoundException e) {
                logger.fine("Multibranch pipeline plugin not installed, skipping detection");
            } catch (LinkageError e) {
                logger.warning("Failed to load multibranch class: " + e.getMessage());
            }
            mbpClassResolved = true;
        }
        return cachedMbpClass != null && cachedMbpClass.isInstance(item);
    }
    }

    /**
     * When Jenkins is started up, {@link Jenkins#loadTasks} is called. At that point the {@code
     * config.xml} file is unmarshaled into a {@link Jenkins} object containing a list of {@link
     * View}s, including {@link BuildMonitorView} objects.
     *
     * <p>The unmarshaling process sets private fields on {@link BuildMonitorView} objects directly,
     * ignoring their constructors. This means that if there's a private field added to this class
     * (say {@code config}), the previously persisted versions of this class can no longer be
     * correctly unmarshaled into the new version as they don't define the new field and the object
     * ends up in an inconsistent state.
     *
     * @return the previously persisted version of the config object, default config, or the
     *     deprecated "order" object, converted to a "config" object.
     */
    private Config currentConfig() {
        if (creatingAFreshView()) {
            config = Config.defaultConfig();
        } else if (deserailisingFromAnOlderFormat()) {
            migrateFromOldToNewConfigFormat();
        }

        return config;
    }

    private boolean creatingAFreshView() {
        return config == null && order == null;
    }

    // Is config.xml saved in a format prior to version 1.6+build.150 of Build Monitor?
    private boolean deserailisingFromAnOlderFormat() {
        return config == null && order != null;
    }

    // If an older version of config.xml is loaded, "config" field is missing, but "order" is present
    private void migrateFromOldToNewConfigFormat() {
        Config c = new Config();

        c.setOrder((BaseOrder) order);

        config = c;
        order = null;
    }

    @SuppressWarnings("unchecked")
    private BaseOrder orderIn(String requestedOrdering) throws ReflectiveOperationException {
        String packageName = this.getClass().getPackage().getName() + ".order.";

        return (BaseOrder) Class.forName(packageName + requestedOrdering)
                .getDeclaredConstructor()
                .newInstance();
    }

    private GetBuildViewModel getBuildViewModelIn(String requestedBuild) throws ReflectiveOperationException {
        String packageName = this.getClass().getPackage().getName() + ".build.";

        return (GetBuildViewModel) Class.forName(packageName + requestedBuild)
                .getDeclaredConstructor()
                .newInstance();
    }

    private Config config;

    @Deprecated // use Config instead
    private Comparator<Job<?, ?>>
            order; // note: this field can be removed when people stop using versions prior to 1.6+build.150
}

