package net.serenitybdd.integration.jenkins.environment.rules;

import org.junit.rules.TestRule;

public interface ApplicativeTestRule<ThingUnderTest> {
    TestRule applyTo(final ThingUnderTest subject);
}
