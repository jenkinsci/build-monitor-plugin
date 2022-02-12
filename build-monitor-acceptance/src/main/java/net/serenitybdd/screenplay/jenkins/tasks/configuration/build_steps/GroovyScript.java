package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class GroovyScript {

    public static GroovyScript that(String descriptionOfScriptsBehaviour) {
        return new GroovyScript(descriptionOfScriptsBehaviour);
    }

    public GroovyScript separatedBy(String lineSeparator) {
        this.lineSeparator = lineSeparator;

        return this;
    }

    public GroovyScript definedAs(String... lines) {
        return this.definedAs(asList(lines));
    }

    public GroovyScript definedAs(List<String> lines) {
        this.code = String.join(this.lineSeparator, lines);

        return this;
    }

    public GroovyScript andOutputs(String... lines) {
        return definedAs(Arrays.stream(lines).map(mapEachLineTo("echo \"%s\";")).collect(Collectors.toList()));
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

    private String lineSeparator = "\n";

    private String code = "";
}
