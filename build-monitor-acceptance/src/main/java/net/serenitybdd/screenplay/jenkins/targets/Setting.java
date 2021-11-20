package net.serenitybdd.screenplay.jenkins.targets;

import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Setting {
	
    public static Target defining(String name) {
        return Target.the(format("the '%s' field", name))
                .locatedBy(lastElementMatching(either(xpathFor("input"), xpathFor("textarea"), xpathFor("select"))))
                .of(name);
    }

    private static String xpathFor(String fieldType) {
        return format("//div[contains(@class, 'tr') and div[contains(@class, 'setting-name') and contains(., '{0}')]]//%s", fieldType);
    }

    private static String either(String... xpaths) {
        return String.join(" | ", xpaths);
    }

    private static String lastElementMatching(String xpath) {
        return format("(%s)[last()]", xpath);
    }
}
