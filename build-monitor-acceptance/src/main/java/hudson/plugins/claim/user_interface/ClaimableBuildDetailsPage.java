package hudson.plugins.claim.user_interface;

import net.serenitybdd.screenplay.jenkins.targets.Button;
import net.serenitybdd.screenplay.jenkins.targets.Link;
import net.serenitybdd.screenplay.jenkins.targets.Setting;
import net.serenitybdd.screenplay.targets.Target;

public class ClaimableBuildDetailsPage {
    public static final Target Claim_It_Link = Link.called("Claim it");
    public static final Target Claim_Button  = Button.called("Claim");
    public static final Target Reason_Field  = Setting.defining("Reason");
}
