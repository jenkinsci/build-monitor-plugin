package net.serenitybdd.integration.jenkins.environment;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.getProperty;
import static net.serenitybdd.integration.utils.Nulls.getOrElse;

public class CWD {
    public static CWD or(String arbitraryCurrentWorkingDirectory) {
        return new CWD(arbitraryCurrentWorkingDirectory);
    }

    public CWD() {
        this(null);
    }

    public CWD(@Nullable String path) {
        cwd = Paths.get(getOrElse(path, getProperty("user.dir")));
    }

    public Path resolve(String pathToChild) {
        return cwd.resolve(pathToChild);
    }

    public Path asPath() {
        return cwd;
    }

    private final Path cwd;
}
