package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

public class GroovyScriptThat {
    public static final GroovyScript Adds_A_Badge = GroovyScript.that("Adds a badge")
            .definedAs("manager.addShortText('Coverage', 'black', 'repeating-linear-gradient(45deg, yellow, yellow 10px, Orange 10px, Orange 20px)', '0px', 'white')");
}