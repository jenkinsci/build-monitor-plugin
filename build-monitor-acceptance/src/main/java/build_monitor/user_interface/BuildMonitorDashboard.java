package build_monitor.user_interface;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import core_jenkins.targets.Link;

public class BuildMonitorDashboard extends PageObject {
    public static final Target Add_Some_Projects_link = Link.called("add some projects");
    public static final Target Project_Widget         = Target.the("Project Widget").locatedBy("//li[header/h2[.='{0}']]");
}
