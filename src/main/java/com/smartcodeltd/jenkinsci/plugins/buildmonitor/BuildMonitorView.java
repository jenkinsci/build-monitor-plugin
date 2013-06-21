package com.smartcodeltd.jenkinsci.plugins.buildmonitor;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.JobView;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.ListView;
import hudson.model.TopLevelItem;
import hudson.model.ViewDescriptor;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.util.ArrayList;
import java.util.List;

public class BuildMonitorView extends ListView {

    /**
     * @param name  Name of the view
     */
    @DataBoundConstructor
    public BuildMonitorView(String name) {
        super(name);
    }

    @Extension
    public static final class Descriptor extends ViewDescriptor {
        public Descriptor() {
            super(BuildMonitorView.class);
        }

        @Override
        public String getDisplayName() {
            return "Build Monitor";
        }
    }

    /**
     * Because of how org.kohsuke.stapler.HttpResponseRenderer is implemented
     * it can only work with net.sf.JSONObject in order to produce correct application/json
     * output
     *
     * @return
     * @throws Exception
     */
    @JavaScriptMethod
    public JSONObject fetchJobViews() throws Exception {
        ObjectMapper m = new ObjectMapper();

        return asJsonObject("{jobs:" + m.writeValueAsString(jobViews()) + "}");
    }

    private JSONObject asJsonObject(String jsonString) {
        return (JSONObject) JSONSerializer.toJSON(jsonString);
    }

    private List<JobView> jobViews() {
        List<JobView> jobs = new ArrayList<JobView>();

        for (TopLevelItem item : super.getItems()) {
            if (item instanceof AbstractProject) {
                AbstractProject project = (AbstractProject) item;
                if (! project.isDisabled()) {
                    jobs.add(JobView.of(project));
                }
            }
        }

        return jobs;
    }
}
