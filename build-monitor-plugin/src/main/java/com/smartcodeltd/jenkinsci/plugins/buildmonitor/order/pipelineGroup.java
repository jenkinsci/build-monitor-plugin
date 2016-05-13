package com.smartcodeltd.jenkinsci.plugins.buildmonitor.order;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.util.BuildUtil;
import hudson.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class pipelineGroup implements Comparator<AbstractProject<?, ?>> {

    @Override
    public int compare(AbstractProject<?, ?> a, AbstractProject<?, ?> b) {
        if (BuildUtil.pipelineBuildNumber(a.getLastBuild()) < BuildUtil.pipelineBuildNumber(b.getLastBuild())) {
            return -1;
        } else if (BuildUtil.pipelineBuildNumber(a.getLastBuild()) > BuildUtil.pipelineBuildNumber(b.getLastBuild())) {
            return 1;
        } else {
            return 0;
        }
    }
}