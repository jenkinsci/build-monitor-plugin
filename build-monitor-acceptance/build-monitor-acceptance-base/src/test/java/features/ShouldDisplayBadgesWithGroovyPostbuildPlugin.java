package features;

import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class ShouldDisplayBadgesWithGroovyPostbuildPlugin extends ShouldDisplayBadgesAbstractBase {

    @Override
    String[] getPlugins() {
        return new String[] {"workflow-cps", "workflow-step-api", "workflow-job", "buildtriggerbadge", "groovy-postbuild"};
    }
}
