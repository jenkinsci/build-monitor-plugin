package net.serenitybdd.screenplay.jenkins.tasks.configuration;

import com.beust.jcommander.internal.Lists;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

import java.util.List;

import static java.util.Arrays.asList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class TodoList implements Task {
    public static TodoList empty() {
        return instrumented(TodoList.class);
    }

    public <T extends Performable> TodoList add(T task) {
        todos.add(task);

        return this;
    }

    public <T extends Performable> TodoList addAll(T... tasks) {
        todos.addAll(asList(tasks));

        return this;
    }

    public <T extends Performable> TodoList addAll(List<T> tasks) {
        todos.addAll(tasks);

        return this;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(perform(todos));
    }

    private Performable[] perform(List<Performable> todos) {
        return todos.toArray(new Performable[todos.size()]);
    }

    private final List<Performable> todos = Lists.newArrayList();
}
