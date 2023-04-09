package net.serenitybdd.integration.jenkins.environment.rules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.serenitybdd.integration.jenkins.JenkinsInstance;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandboxJenkinsHome implements ApplicativeTestRule<JenkinsInstance> {
    private static final Logger Log = LoggerFactory.getLogger(SandboxJenkinsHome.class);

    private final Path rootDirectory;

    public static SandboxJenkinsHome useATemporaryDirectoryUnder(Path rootDirectory) {
        return new SandboxJenkinsHome(rootDirectory);
    }

    public SandboxJenkinsHome(Path rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public TestRule applyTo(final JenkinsInstance jenkins) {
        return new TestWatcher() {
            @Override
            protected void starting(Description test) {

                Path jenkinsHome = temporaryJenkinsHomeFor(test);

                Log.info("Setting jenkins home to {}", jenkinsHome);

                jenkins.setHome(jenkinsHome);
            }
        };
    }

    private Path temporaryJenkinsHomeFor(Description test) {
        try {
            return Files.createTempDirectory(Files.createDirectories(rootDirectory), fileSystemSafeNameOf(test) + "_");
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Couldn't create a temporary directory for '%s' at '%s'.", test, rootDirectory), e);
        }
    }

    private String fileSystemSafeNameOf(Description description) {
        return String.format("%s_%s", simplified(description.getClassName()), description.getMethodName());
    }

    private String simplified(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }
}
