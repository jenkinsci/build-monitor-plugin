package net.serenitybdd.screenplay.jenkins.actions;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import java.util.Arrays;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplayx.actions.Evaluate;
import net.thucydides.core.annotations.Step;

public class EnterCode {
    public static EnterCode asFollows(String... lines) {
        return new EnterCode(Arrays.asList(lines));
    }

    public Interaction intoTheCodeMirror(Target editorField) {
        return instrumented(EnterCodeIntoCodeMirrorEditor.class, editorField, String.join(System.lineSeparator(), lines));
    }

    public Interaction intoThePipelineEditor(Target editorField) {
        return instrumented(EnterCodeIntoPipelineEditor.class, editorField, String.join(System.lineSeparator(), lines));
    }

    public EnterCode(List<String> lines) {
        this.lines = lines;
    }

    private final List<String> lines;

    public static class EnterCodeIntoCodeMirrorEditor implements Interaction {

        private final Target target;
        private final String code;

        public EnterCodeIntoCodeMirrorEditor(Target target, String code) {
            this.target = target;
            this.code  = code;
        }

        @Override
        @Step("{0} enters '#code' into the code editor field")
        public <T extends Actor> void performAs(T actor) {
            actor.attemptsTo(
              Click.on(target),
              Evaluate.javascript(
                    setCodeMirrorValueTo(code),
                    target.resolveFor(actor)
            ));
        }

        private String setCodeMirrorValueTo(String code) {
            return String.format(
                    "var code_mirror = arguments[0].CodeMirror;" +
                            "if (code_mirror != null) { " +
                            "    code_mirror.setValue('%s');" +
                            "    code_mirror.save();" +
                            "} else { " +
                            "    console.error('CodeMirror object is not present on the', arguments[0], 'element'); " +
                            "}",
                    escapeNewLineCharacters(code));
        }

        private String escapeNewLineCharacters(String code) {
            return code.replaceAll("\n", "\\\\n");
        }
    }

    private static class EnterCodeIntoPipelineEditor implements Interaction {

        private final Target target;
        private final String code;

        public EnterCodeIntoPipelineEditor(Target target, String code) {
            this.target = target;
            this.code  = code;
        }

        @Override
        @Step("{0} enters '#code' into the pipeline editor field")
        public <T extends Actor> void performAs(T actor) {
            target.resolveFor(actor).type(code);
        }

    }
}
