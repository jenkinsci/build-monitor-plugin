package features;

import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.runner.RunWith;

@RunWith(SerenityRunner.class)
public class ShouldDisplayBadgesWithBadgePlugin extends ShouldDisplayBadgesAbstractBase {

    @Override
    String[] getPlugins() {
        return new String[] {"workflow-aggregator", "buildtriggerbadge", "badge", "groovy-postbuild"};
    }
}
