package com.smartcodeltd.jenkinsci.plugins.buildmonitor.api;

import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import static com.smartcodeltd.jenkinsci.plugins.buildmonitor.api.Success.successful;
import static net.sf.json.JSONSerializer.toJSON;

public class Respond {
    private Respond(){}

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> JSONObject withSuccess(T responseData) throws IOException {
        return (JSONObject) toJSON(mapper.writeValueAsString(successful(responseData)));
    }
}
