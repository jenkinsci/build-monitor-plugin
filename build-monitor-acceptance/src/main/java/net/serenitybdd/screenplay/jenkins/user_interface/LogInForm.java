package net.serenitybdd.screenplay.jenkins.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Input;
import net.serenitybdd.screenplay.targets.Target;

public class LogInForm {
    public static final Target Username_Field = Input.named("j_username");
    public static final Target Password_Field = Input.named("j_password");
    public static final Target Log_In_Buttton = Target.the("the 'Sign In' button").locatedBy("//button[@type='submit']");
}
