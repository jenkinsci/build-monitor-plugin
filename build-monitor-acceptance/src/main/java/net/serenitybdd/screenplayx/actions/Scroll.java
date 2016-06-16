package net.serenitybdd.screenplayx.actions;

import net.serenitybdd.screenplay.targets.Target;

public class Scroll {
    private Scroll(){}
    public static ScrollToTarget to(Target target) {
        return new ScrollToTarget(target);
    }
}
