package net.serenitybdd.screenplay.jenkins.tasks.configuration.build_steps;

public class GroovyScriptThat {
    public static final GroovyScript Adds_A_Badge = GroovyScript.that("Adds a badge")
            .definedAs("addShortText(text:'Coverage', color:'black', background:'repeating-linear-gradient(45deg, yellow, yellow 10px, Orange 10px, Orange 20px)', border: 0, borderColor:'white')");
}