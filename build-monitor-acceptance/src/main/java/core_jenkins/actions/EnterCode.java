package core_jenkins.actions;

import com.google.common.base.Joiner;
import net.serenitybdd.screenplay.Action;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

import static java.util.Arrays.asList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class EnterCode {
    public static EnterCode asFollows(String... lines) {
        return new EnterCode(asList(lines));
    }

    public Action into(Target codeMirrorEditorField) {
        return instrumented(EnterCodeIntoTarget.class, codeMirrorEditorField, Joiner.on(System.lineSeparator()).join(lines));
    }

    public EnterCode(List<String> lines) {
        this.lines = lines;
    }

    private final List<String> lines;

    public static class EnterCodeIntoTarget implements Action {

        private final Target target;
        private final String code;

        public EnterCodeIntoTarget(Target target, String code) {
            this.target = target;
            this.code  = code;
        }

        @Override
        @Step("{0} enters '#code' into the code editor field")
        public <T extends Actor> void performAs(T actor) {
            // this works
            WebElement element = target.resolveFor(actor).getWrappedElement();
            ((JavascriptExecutor) BrowseTheWeb.as(actor).getDriver()).executeScript(
                    String.format(
                            "var code_mirror = arguments[0].CodeMirror;" +
                            "if (code_mirror != null) { " +
                            "    code_mirror.setValue('%s');" +
                            "    code_mirror.save();" +
                            "} else { " +
                            "    console.error('CodeMirror object is not present on the', arguments[0], 'element'); " +
                            "}",
                    code),
                    element
            );


            // fixme: executing the below causes a JavaScript error, breaking Jenkins form validation.
            // not sure why, is there a side effect to Evaluate.javascript?

//            actor.attemptsTo(
//                    Evaluate.javascript(String.format(
//                            "var code_mirror = arguments[0].CodeMirror;" +
//                                    "if (code_mirror != null) { " +
//                                    "    code_mirror.setValue('%s');" +
//                                    "    code_mirror.save();" +
//                                    "} else { console.error('CodeMirror object not found on the', arguments[0], 'element'); }",
//                            code),
//                            target.resolveFor(actor)
//                    )
//            );
        }
    }
}
