package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel;

import com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.features.Feature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Jan Molak
 */
public class JobViewSerialiser extends JsonSerializer<JobView> {
    @Override
    public void serialize(JobView job, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("name",               job.name());
        jgen.writeObjectField("url",                job.url());
        jgen.writeObjectField("status",             job.status());
        jgen.writeObjectField("hashCode",           job.hashCode());
        jgen.writeObjectField("progress",           job.progress());
        jgen.writeObjectField("estimatedDuration",  job.estimatedDuration());

        for (Feature<?> feature : job.features()) {
            Object serialised = feature.asJson();

            if (serialised != null) {
                jgen.writeObjectField(nameOf(serialised), serialised);
            }
        }

        jgen.writeEndObject();
    }

    private String nameOf(Object serialised) {
        // http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/beans/Introspector.java#265
        char[] chars = serialised.getClass().getSimpleName().toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);

        return new String(chars);
    }
}
