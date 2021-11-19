package net.serenitybdd.screenplayx.actions;

import net.serenitybdd.screenplay.Action;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.WebElement;

public class ScrollToTarget implements Action {
    private final Target target;
    private boolean alignToTop = true;

    public ScrollToTarget(Target target) {
        this.target = target;
    }

    public ScrollToTarget andAlignToTop() {
        this.alignToTop = true;

        return this;
    }

    public ScrollToTarget andAlignToBottom() {
        this.alignToTop = false;

        return this;
    }

    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                scrollTo(target.resolveFor(actor), alignToTop)
        );
    }

    private Evaluate scrollTo(WebElement element, boolean alignToTop) {
        return Evaluate.javascript("arguments[0].scrollIntoView(arguments[1]);", element, alignToTop);
    }
}
