package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShellScript {

    public static ShellScript that(String descriptionOfScriptsBehaviour) {
        return new ShellScript(descriptionOfScriptsBehaviour);
    }

    public ShellScript definedAs(String... lines) {
        return this.definedAs(List.of(lines));
    }

    public ShellScript definedAs(List<String> lines) {
        this.code = String.join("\n", lines);

        return this;
    }

    public ShellScript andOutputs(String... lines) {
        return definedAs(Stream.of(lines)
                .map(line -> String.format("echo \"%s\";", line))
                .collect(Collectors.toList()));
    }

    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return description;
    }

    private ShellScript(String descriptionOfScriptsBehaviour) {
        this.description = descriptionOfScriptsBehaviour;
    }

    private final String description;

    private String code = "";
}
