package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.claim;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.plugins.Augmentation;

public interface Claim extends Augmentation {
    public boolean wasMade();
    public String author();
    public String reason();
}
