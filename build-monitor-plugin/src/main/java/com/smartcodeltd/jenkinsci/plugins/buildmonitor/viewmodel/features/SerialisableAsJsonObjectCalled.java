package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features;

/**
 * @author Jan Molak
 */
public interface SerialisableAsJsonObjectCalled<JSON extends Object> {
    JSON asJson();
}
