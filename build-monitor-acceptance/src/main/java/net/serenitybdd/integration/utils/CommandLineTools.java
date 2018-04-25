package net.serenitybdd.integration.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.serenitybdd.integration.utils.Nulls.coalesce;

public class CommandLineTools {
    public static Path java() {
        String javaHomeString = coalesce(System.getenv("JENKINS_JAVA_HOME"), System.getenv("JAVA_HOME"),
          System.getProperty("java.home"));
        if (javaHomeString == null) {
            throw new RuntimeException("'java' executable not found. Please set the JAVA_HOME env variable to point to your Java home directory.");
        }
        Path javaHome = Paths.get(javaHomeString);

        Path javaBin;
        if (Files.exists(javaHome.resolve("bin/java"))) {
        	javaBin = javaHome.resolve("bin/java");
        } else if (Files.exists(javaHome.resolve("bin/java.exe"))) {
        	javaBin = javaHome.resolve("bin/java.exe");
        } else {
            throw new RuntimeException("'java' executable not found. Please set the JAVA_HOME env variable to point to your Java home directory.");
        }
        if (!Files.isExecutable(javaBin)) {
            throw new RuntimeException("'java' executable not found. Please set the JAVA_HOME env variable to point to your Java home directory.");
        }
        return javaBin;
    }
}
