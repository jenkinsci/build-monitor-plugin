package build_monitor.tasks;

import core_jenkins.actions.Choose;
import com.beust.jcommander.internal.Lists;
import net.serenitybdd.screenplay.Action;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import user_interface.navigation.Buttons;
import build_monitor.user_interface.BuildMonitorViewConfigurationPage;
import build_monitor.user_interface.BuildMonitorDashboard;

import java.util.List;

import static com.beust.jcommander.internal.Lists.newArrayList;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConfigureBuildMonitorView implements Task {

    @Override
    @Step("{0} configures the Build Monitor View")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(applyConfiguration(actions));
    }

    public static ConfigureBuildMonitorView toDisplayAllProjects() {
        return instrumented(ConfigureBuildMonitorView.class, newArrayList(
                Choose.the(BuildMonitorViewConfigurationPage.Use_Regular_Expression),
                Enter.theValue(".*").into(BuildMonitorViewConfigurationPage.Regular_Expression)
        ));
    }

    public ConfigureBuildMonitorView(List<Action> actions) {
        this.actions = actions;
    }

    private Action[] applyConfiguration(List<Action> actions) {
        // todo: messy, clean up
        List<Action> todos = Lists.newArrayList();

        todos.add(Click.on(BuildMonitorDashboard.Add_Some_Projects_link));
        todos.addAll(actions);
        todos.add(Click.on(Buttons.OK));

        // todo: extract to some utility method
//        return todos.stream().toArray(size -> new Action[size]);
        return todos.toArray(new Action[todos.size()]);
    }

    private List<Action> actions = newArrayList();
}
