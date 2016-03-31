package net.serenitybdd.screenplay.jenkins.targets;

import com.google.common.base.Joiner;
import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Setting {
    public static Target defining(String name) {
        String xpath = "//tr[td[contains(@class, 'setting-name') and contains(., '{0}')]]//%s";

        return Target.the(format("the '%s' setting", name))
                .locatedBy(either(format(xpath, "input"), format(xpath, "textarea")))
                .of(name);
    }

    private static String either(String... xpaths) {
        return Joiner.on(" | ").join(xpaths);
    }

    public Target ofType(String elementType) {
        return Target.the(format("the '%s' setting", name))
                .locatedBy("//tr[td[contains(@class, 'setting-name') and contains(., '{0}')]]//{1}")
                .of(name, elementType);
    }

    private final String name;

    public Setting(String name) {
        this.name = name;
    }
}
