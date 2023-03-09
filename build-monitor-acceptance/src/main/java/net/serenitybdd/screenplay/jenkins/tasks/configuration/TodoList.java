package net.serenitybdd.screenplay.jenkins.tasks.configuration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import java.util.ArrayList;
import java.util.List;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

public class TodoList implements Task {
    public static TodoList empty() {
        return instrumented(TodoList.class);
    }

    public <T extends Performable> TodoList add(T task) {
        todos.add(task);

        return this;
    }

    public <T extends Performable> TodoList addAll(T... tasks) {
        todos.addAll(List.of(tasks));

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
        return todos.toArray(new Performable[0]);
    }

    private final List<Performable> todos = new ArrayList<>();
}
