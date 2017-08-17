package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.targets.Target;

public class EditUserDefinedAxis {
    public static final Target EditAxisName = Target.the("code editor").locatedBy("(//div[@descriptorid='hudson.matrix.TextAxis']//input[@class='setting-input validated  '])");
    public static final Target EditAxisVals = Target.the("code editor").locatedBy("(//div[@descriptorid='hudson.matrix.TextAxis']//input[@class='setting-input'])");
}