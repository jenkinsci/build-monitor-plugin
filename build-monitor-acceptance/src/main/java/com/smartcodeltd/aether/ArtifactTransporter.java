package com.smartcodeltd.aether;

import net.serenitybdd.integration.jenkins.environment.rules.FindFreePort;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

public class ArtifactTransporter {

    private static final Logger Log = LoggerFactory.getLogger(ArtifactTransporter.class);

    private final List<RemoteRepository> remoteLocations;
    private final Path localRepositoryLocation;


    public ArtifactTransporter(Path localRepositoryLocation, RemoteRepository... remoteLocations) {
        this.localRepositoryLocation = localRepositoryLocation;
        this.remoteLocations         = Arrays.asList(remoteLocations);

        // todo: proxies: https://github.com/jenkinsci/acceptance-test-harness/blob/b27c150f35a386dfced4a5960127bf62f9c34363/src/main/java/org/jenkinsci/test/acceptance/utils/aether/ArtifactResolverUtil.java
    }

    public Path get(String gav, String extension) {
        String[] t = gav.split(":");

        return get(t[0], t[1], t[2], extension);
    }

    public Path get(@NotNull String groupName, @NotNull String artifactName, @NotNull String version, @NotNull String artifactFileExtension) {
        Artifact artifact = new DefaultArtifact(groupName, artifactName, artifactFileExtension, version);

        RepositorySystem system = newRepositorySystem();
        RepositorySystemSession session = newRepositorySystemSession(system);

        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact(artifact);
        request.setRepositories(repositories(system, session));

        try {
            ArtifactResult artifactResult = system.resolveArtifact(session, request);

            artifact = artifactResult.getArtifact();

            Log.info(artifact + " resolved to  " + artifact.getFile());

            return artifact.getFile().toPath();

        } catch (ArtifactResolutionException e) {
            throw new RuntimeException(format("Couldn't resolve a '%s' artifact for '%s:%s:%s'",
                    artifactFileExtension, groupName, artifactName, version
            ), e);
        }
    }

    private RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                throw new RuntimeException("Service creation failed", exception);
            }
        });

        return locator.getService(RepositorySystem.class);
    }

    private DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository(localRepositoryLocation.toAbsolutePath().toString());
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());

        return session;
    }

    private List<org.eclipse.aether.repository.RemoteRepository> repositories(RepositorySystem system, RepositorySystemSession session) {
        List<org.eclipse.aether.repository.RemoteRepository> repositories = new ArrayList<>();

        for (RemoteRepository location : remoteLocations) {
            repositories.add(new org.eclipse.aether.repository.RemoteRepository.Builder(
                    location.id(),
                    location.type(),
                    location.url()
            ).build());
        }

        return repositories;
    }
}
