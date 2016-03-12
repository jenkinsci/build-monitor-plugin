package core_jenkins.user_interface;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import net.thucydides.core.annotations.DefaultUrl;
import core_jenkins.targets.Link;

@DefaultUrl("http://localhost:8080/")
public class JenkinsHomePage extends PageObject {
    public static final Target New_View_link = Link.called("+");
}