package net.serenitybdd.integration.jenkins.environment.rules;

import net.serenitybdd.integration.jenkins.JenkinsInstance;
import net.serenitybdd.screenplay.jenkins.JenkinsUser;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class RegisterUserAccount implements ApplicativeTestRule<JenkinsInstance> {
    private final JenkinsUser user;

    public RegisterUserAccount(JenkinsUser user) {
        this.user = user;
    }

    public static RegisterUserAccount of(JenkinsUser user) {
        return new RegisterUserAccount(user);
    }

    @Override
    public TestRule applyTo(final JenkinsInstance jenkins) {
        return new TestWatcher() {
            @Override
            protected void starting(Description description) {
                jenkins.client().registerAccount(user.getName(), user.password());
            }
        };
    }
}
