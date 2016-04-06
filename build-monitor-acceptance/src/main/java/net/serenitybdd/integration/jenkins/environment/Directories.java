package net.serenitybdd.integration.jenkins.environment;

import sun.security.action.GetPropertyAction;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.security.AccessController.doPrivileged;

public class Directories {
    public static final Path Default_Temp_Dir =
            Paths.get(doPrivileged(new GetPropertyAction("java.io.tmpdir")));
}
