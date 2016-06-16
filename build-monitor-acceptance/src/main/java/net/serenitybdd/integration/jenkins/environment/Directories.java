package net.serenitybdd.integration.jenkins.environment;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.getProperty;

public class Directories {
    private Directories(){}
    public static final Path Default_Temp_Dir = Paths.get(getProperty("java.io.tmpdir"));
}
