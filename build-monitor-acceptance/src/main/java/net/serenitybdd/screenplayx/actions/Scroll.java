package net.serenitybdd.screenplayx.actions;

import net.serenitybdd.screenplay.targets.Target;

public class Scroll {
    public static ScrollToTarget to(Target target) {
        return new ScrollToTarget(target);
    }
}
