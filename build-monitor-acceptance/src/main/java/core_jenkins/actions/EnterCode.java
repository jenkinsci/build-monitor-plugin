package core_jenkins.actions;

import com.google.common.base.Joiner;
import net.serenitybdd.screenplay.Action;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplayx.actions.Evaluate;
import net.thucydides.core.annotations.Step;

import java.util.List;

import static java.util.Arrays.asList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class EnterCode {
    public static EnterCode asFollows(String... lines) {
        return new EnterCode(asList(lines));
    }

    public Action intoTheCodeMirror(Target editorField) {
        return instrumented(EnterCodeIntoCodeMirrorEditor.class, editorField, Joiner.on(System.lineSeparator()).join(lines));
    }

    public EnterCode(List<String> lines) {
        this.lines = lines;
    }

    private final List<String> lines;

    public static class EnterCodeIntoCodeMirrorEditor implements Action {

        private final Target target;
        private final String code;

        public EnterCodeIntoCodeMirrorEditor(Target target, String code) {
            this.target = target;
            this.code  = code;
        }

        @Override
        @Step("{0} enters '#code' into the code editor field")
        public <T extends Actor> void performAs(T actor) {
            actor.attemptsTo(Evaluate.javascript(
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
                code);
        }
    }
}
