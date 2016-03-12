package build_monitor.user_interface;

import net.serenitybdd.core.annotations.findby.By;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import core_jenkins.targets.Checkbox;

public class BuildMonitorViewConfigurationPage extends PageObject {
    public static final Target Use_Regular_Expression = Checkbox.withLabel("Use a regular expression to include jobs into the view");
    public static final Target Regular_Expression     = Target.the("'Regular expression' field").located(By.name("includeRegex"));
}
