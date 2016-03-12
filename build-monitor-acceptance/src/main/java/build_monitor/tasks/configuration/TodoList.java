package build_monitor.tasks.configuration;

import com.beust.jcommander.internal.Lists;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class TodoList implements Task {
    public static TodoList empty() {
        return instrumented(TodoList.class);
    }

    public TodoList add(Performable task) {
        todos.add(task);

        return this;
    }

    public TodoList addAll(List<Performable> tasks) {
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
