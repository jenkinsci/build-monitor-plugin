package net.serenitybdd.screenplay.jenkins.targets;

import com.google.common.base.Joiner;
import net.serenitybdd.screenplay.targets.Target;

import static java.lang.String.format;

public class Setting {
	
    public static Target defining(String name) {
        return Target.the(format("the '%s' field", name))
                .locatedBy(lastElementMatching(either(xpathFor("input"), xpathFor("textarea"), xpathFor("select"), 
                        xpathForTableLayout("input"), xpathForTableLayout("textarea"), xpathForTableLayout("select"))))
                .of(name);
    }

    private static String xpathForTableLayout(String fieldType) {
        // TODO: Deprecated Layout Since 2.277.1
        return format("//tr[td[contains(@class, 'setting-name') and contains(., '{0}')]]//%s", fieldType);
    }

    private static String xpathFor(String fieldType) {
        return format("//div[contains(@class, 'tr') and div[contains(@class, 'setting-name') and contains(., '{0}')]]//%s", fieldType);
    }

    private static String either(String... xpaths) {
        return Joiner.on(" | ").join(xpaths);
    }

    private static String lastElementMatching(String xpath) {
        return format("(%s)[last()]", xpath);
    }
}
