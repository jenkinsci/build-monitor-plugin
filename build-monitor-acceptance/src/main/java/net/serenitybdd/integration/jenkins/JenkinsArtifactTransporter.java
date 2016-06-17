package net.serenitybdd.integration.jenkins;

import com.smartcodeltd.aether.ArtifactTransporter;
import com.smartcodeltd.aether.RemoteRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JenkinsArtifactTransporter {
    private JenkinsArtifactTransporter(){}

    public static ArtifactTransporter create() {
        return new ArtifactTransporter(
                pathToLocalMavenRepository(),
                RemoteRepository.at("http://repo.jenkins-ci.org/public/")
        );
    }

    private static Path pathToLocalMavenRepository() {
        return Paths.get(System.getProperty("user.home"), ".m2", "repository");
    }
}
