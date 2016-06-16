package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

public class ShellScriptThat {
    private ShellScriptThat(){}
    public static final ShellScript Finishes_With_Success = ShellScript.that("Finishes with success").definedAs("exit 0;");
    public static final ShellScript Finishes_With_Error   = ShellScript.that("Finishes with error").definedAs("exit 1;");
}