package net.serenitybdd.screenplayx.actions;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Action;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.JavascriptExecutor;

// the original implementation has a side-effect of injecting the jQuery library, which conflicts with Jenkins' prototype.js
public class Evaluate implements Action {

    private final String expression;
    private Object[] parameters;

    public Evaluate(String expression) {
        this.expression = expression;
    }

    public Evaluate withParameters(Object... parameters) {
        this.parameters = parameters;
        return this;
    }

    @Step("Execute JavaScript #expression")
    public <T extends Actor> void performAs(T theUser) {
        ((JavascriptExecutor) BrowseTheWeb.as(theUser).getDriver()).executeScript(expression, parameters);
    }

    public static Evaluate javascript(String expression, Object... parameters) {
        Evaluate evaluate = Instrumented.instanceOf(Evaluate.class).withProperties(expression);
        return evaluate.withParameters(parameters);
    }
}
