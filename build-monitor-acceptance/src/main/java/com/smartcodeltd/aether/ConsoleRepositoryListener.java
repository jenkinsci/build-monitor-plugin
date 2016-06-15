package com.smartcodeltd.aether;

import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;

import java.io.PrintStream;

public class ConsoleRepositoryListener extends AbstractRepositoryListener {
    private static final String FROM= " from ";

    private PrintStream out;

    public ConsoleRepositoryListener() {
        this(null);
    }

    public ConsoleRepositoryListener(PrintStream out) {
        this.out = (out != null) ? out : System.out;
    }

    public void artifactDeployed(RepositoryEvent event) {
        out.println(">> Deployed " + event.getArtifact() + " to " + event.getRepository());
    }

    public void artifactDeploying(RepositoryEvent event) {
        out.println(">> Deploying " + event.getArtifact() + " to " + event.getRepository());
    }

    public void artifactDescriptorInvalid(RepositoryEvent event) {
        out.println(">> Invalid artifact descriptor for " + event.getArtifact() + ": "
                + event.getException().getMessage());
    }

    public void artifactDescriptorMissing(RepositoryEvent event) {
        out.println(">> Missing artifact descriptor for " + event.getArtifact());
    }

    public void artifactInstalled(RepositoryEvent event) {
        out.println(">> Installed " + event.getArtifact() + " to " + event.getFile());
    }

    public void artifactInstalling(RepositoryEvent event) {
        out.println(">> Installing " + event.getArtifact() + " to " + event.getFile());
    }

    public void artifactResolved(RepositoryEvent event) {
        out.println(">> Resolved artifact " + event.getArtifact() + FROM + event.getRepository());
    }

    public void artifactDownloading(RepositoryEvent event) {
        out.println(">> Downloading artifact " + event.getArtifact() + FROM + event.getRepository());
    }

    public void artifactDownloaded(RepositoryEvent event) {
        out.println(">> Downloaded artifact " + event.getArtifact() + FROM + event.getRepository());
    }

    public void artifactResolving(RepositoryEvent event) {
        out.println(">> Resolving artifact " + event.getArtifact());
    }

    public void metadataDeployed(RepositoryEvent event) {
        out.println(">> Deployed " + event.getMetadata() + " to " + event.getRepository());
    }

    public void metadataDeploying(RepositoryEvent event) {
        out.println(">> Deploying " + event.getMetadata() + " to " + event.getRepository());
    }

    public void metadataInstalled(RepositoryEvent event) {
        out.println(">> Installed " + event.getMetadata() + " to " + event.getFile());
    }

    public void metadataInstalling(RepositoryEvent event) {
        out.println(">> Installing " + event.getMetadata() + " to " + event.getFile());
    }

    public void metadataInvalid(RepositoryEvent event) {
        out.println(">> Invalid metadata " + event.getMetadata());
    }

    public void metadataResolved(RepositoryEvent event) {
        out.println(">> Resolved metadata " + event.getMetadata() + FROM + event.getRepository());
    }

    public void metadataResolving(RepositoryEvent event) {
        out.println(">> Resolving metadata " + event.getMetadata() + FROM + event.getRepository());
    }

}
