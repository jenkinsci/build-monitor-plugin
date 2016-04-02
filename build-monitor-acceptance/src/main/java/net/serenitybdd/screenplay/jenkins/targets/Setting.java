package net.serenitybdd.screenplay.jenkins.targets;

import com.google.common.base.Joiner;
import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Setting {
    public static Target defining(String name) {
        return Target.the(format("the '%s' field", name))
                .locatedBy(either(xpathFor("input"), xpathFor("textarea")))
                .of(name);
    }

    private static String xpathFor(String fieldType) {
        return format("//tr[td[contains(@class, 'setting-name') and contains(., '{0}')]]//%s", fieldType);
    }

    private static String either(String... xpaths) {
        return Joiner.on(" | ").join(xpaths);
    }
}
