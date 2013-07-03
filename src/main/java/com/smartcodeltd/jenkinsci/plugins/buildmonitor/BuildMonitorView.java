/*
 * The MIT License
 *
 * Copyright (c) 2012, Jan Molak, SmartCode Ltd http://smartcodeltd.co.uk
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

/**
 * @author Jan Molak
 */
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
