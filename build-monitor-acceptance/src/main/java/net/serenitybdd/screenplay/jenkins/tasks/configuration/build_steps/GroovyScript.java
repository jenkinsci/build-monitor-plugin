package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroovyScript {

    public static GroovyScript that(String descriptionOfScriptsBehaviour) {
        return new GroovyScript(descriptionOfScriptsBehaviour);
    }

    public GroovyScript separatedBy(String lineSeparator) {
        this.lineSeparator = lineSeparator;

        return this;
    }

    public GroovyScript definedAs(String... lines) {
        return this.definedAs(List.of(lines));
    }

    public GroovyScript definedAs(List<String> lines) {
        this.code = String.join(this.lineSeparator, lines);

        return this;
    }

    public GroovyScript andOutputs(String... lines) {
        return definedAs(Stream.of(lines).map(line -> String.format("echo \"%s\";", line)).collect(Collectors.toList()));
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

    private final String description;

    private String lineSeparator = "\n";

    private String code = "";
}
