package net.serenitybdd.integration.jenkins.environment;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Directories {
    public static final Path Default_Temp_Dir = Paths.get(System.getProperty("java.io.tmpdir"));
}
