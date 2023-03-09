package net.serenitybdd.screenplayx.actions;

import java.util.HashMap;
import java.util.Map;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.WebElement;

public class ScrollToTarget implements Interaction {
    
    private static final Map<String, String> centerAlignOptions;
    
    static {
        centerAlignOptions = new HashMap<>();
        centerAlignOptions.put("behavior", "auto");
        centerAlignOptions.put("block", "center");
        centerAlignOptions.put("inline", "center");
    }
    
    private final Target target;
    private String alignTo = "middle";

    public ScrollToTarget(Target target) {
        this.target = target;
    }

    public ScrollToTarget andAlignToTop() {
        this.alignTo = "top";

        return this;
    }

    public ScrollToTarget andAlignToBottom() {
        this.alignTo = "bottom";

        return this;
    }

    public ScrollToTarget andAlignInMiddle() {
        this.alignTo = "middle";

        return this;
    }

    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(scrollTo(target.resolveFor(actor), alignTo)
        );
    }

    private Evaluate scrollTo(WebElement element, String alignTo) {
        Object args;
        switch (alignTo) {
        case "top":
            args = Boolean.TRUE;
            break;
        case "bottom":
            args = Boolean.FALSE;
            break;
        default:
            args = centerAlignOptions;
            break;
        }
        
        return Evaluate.javascript("arguments[0].scrollIntoView(arguments[1]);", element, args);
    }
}
