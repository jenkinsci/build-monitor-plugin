package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

public class GroovyScript {

    public static GroovyScript that(String descriptionOfScriptsBehaviour) {
        return new GroovyScript(descriptionOfScriptsBehaviour);
    }

    public GroovyScript definedAs(String... lines) {
        return this.definedAs(asList(lines));
    }

    public GroovyScript definedAs(List<String> lines) {
        this.code = Joiner.on('\n').join(lines);

        return this;
    }

    public GroovyScript andOutputs(String... lines) {
        return definedAs(transform(asList(lines), mapEachLineTo("echo \"%s\";")));
    }

    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return description;
    }

    private GroovyScript(String descriptionOfScriptsBehaviour) {
        this.description = descriptionOfScriptsBehaviour;
    }

    private Function<String, String> mapEachLineTo(final String template) {
        return new Function<String, String>() {
            @Nullable
            @Override
            public String apply(@Nullable String line) {
                return String.format(template, line);
            }
        };
    }

    private final String description;

    private String code = "";
}
