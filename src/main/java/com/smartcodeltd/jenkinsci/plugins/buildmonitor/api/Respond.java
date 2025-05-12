package com.smartcodeltd.jenkinsci.plugins.buildmonitor.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Respond {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> JSONObject withSuccess(T responseData) throws IOException {
        return (JSONObject) JSONSerializer.toJSON(mapper.writeValueAsString(Success.successful(responseData)));
    }
}
