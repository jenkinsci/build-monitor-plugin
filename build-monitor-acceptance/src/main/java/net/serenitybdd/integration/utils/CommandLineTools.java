package net.serenitybdd.integration.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.serenitybdd.integration.utils.Nulls.coalesce;

public class CommandLineTools {
    public static Path java() {
        Path javaBin = Paths.get(coalesce(
                System.getenv("JENKINS_JAVA_HOME"),
                System.getenv("JAVA_HOME"),
                System.getProperty("java.home")
        )).resolve("bin/java");

        if (! Files.isExecutable(javaBin)) {
            throw new RuntimeException("'java' executable not found. Please set the JAVA_HOME env variable to point to your Java home directory.");
        }

        return javaBin;
    }
}
