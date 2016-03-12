package build_monitor.model;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

public enum ProjectStatus {
    Unknown("unknown");

    private final String value;

    ProjectStatus(String value) {
        this.value = value;
    }


    // ---- todo: extract

    // todo: can I use https://github.com/serenity-bdd/serenity-web-todomvc-journey/blob/e092e3a90a280aa16b0c1458f0ac7d36fe833f52/src/main/java/net/serenitybdd/demos/todos/questions/CurrentFilter.java ?
    // asEnum thingy

    public static List<ProjectStatus> fromMultiple(String cssClasses) {
        // todo: Java 8?
        List<ProjectStatus> statuses = newArrayList();

        for(String statusClass : Sets.intersection(projectStatusClasses(), setOf(split(cssClasses)))) {
            statuses.add(ProjectStatus.from(statusClass));
        }

        return statuses;
    }

    public static ProjectStatus from(@NotNull String cssClass) {

        for (ProjectStatus status : ProjectStatus.values()) {
            if (cssClass.equalsIgnoreCase(status.value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(format("'%s' is not a recognised value of the ProjectStatus enum", cssClass));
    }

    // todo: Java 8?

    private static Set<String> projectStatusClasses() {
        // todo: can I use that instead? EnumValues.forType(ProjectStatus.class);

        return setOf(stringRepresentationsOf(EnumSet.allOf(ProjectStatus.class)));
    }

    private static List<String> split(String spaceSeparatedItems) {
        return Splitter.on(" ").splitToList(spaceSeparatedItems);
    }

    private static <T> Set<T> setOf(List<T> items) {
        return ImmutableSet.copyOf(items);
    }

    private static <T> List<String> stringRepresentationsOf(Collection<T> items) {
        // todo: Java 8?
        List<String> values = newArrayList();

        for (T item : items) {
            values.add(item.toString());
        }

        return values;
    }


    @Override
    public String toString() {
        return value;
    }
}
