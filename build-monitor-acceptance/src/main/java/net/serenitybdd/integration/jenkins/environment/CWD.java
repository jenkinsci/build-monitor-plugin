package net.serenitybdd.integration.jenkins.environment;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.serenitybdd.integration.utils.Nulls;

public class CWD {
    public static CWD or(String arbitraryCurrentWorkingDirectory) {
        return new CWD(arbitraryCurrentWorkingDirectory);
    }

    public CWD() {
        this(null);
    }

    public CWD(@Nullable String path) {
        cwd = Paths.get(Nulls.getOrElse(path, System.getProperty("user.dir")));
    }

    public Path resolve(String pathToChild) {
        return cwd.resolve(pathToChild);
    }

    public Path asPath() {
        return cwd;
    }

    private final Path cwd;
}
