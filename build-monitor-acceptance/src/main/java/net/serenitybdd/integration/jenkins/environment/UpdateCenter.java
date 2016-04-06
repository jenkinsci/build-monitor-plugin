package net.serenitybdd.integration.jenkins.environment;

import com.google.common.base.Charsets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpdateCenter {
    private final static String Update_Center_URL_Template
            = "https://updates.jenkins-ci.org/update-center.json?id=default&version=%s";
    private final Path tempDir;

    public UpdateCenter() {
        this(Directories.Default_Temp_Dir);
    }

    public UpdateCenter(Path tempDir) {
        this.tempDir = tempDir;
    }

    public String jsonFor(String jenkinsVersion) {
        try {
            return stripJSONPEnvelope(download(updateCenterJSONPFor(jenkinsVersion)));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't download 'update-center.json'.", e);
        }
    }

    private URL updateCenterJSONPFor(String jenkinsVersion) throws MalformedURLException {
        return url(Update_Center_URL_Template, jenkinsVersion);
    }

    private String stripJSONPEnvelope(Path jsonp) throws IOException {
        return Files.readAllLines(jsonp, Charsets.UTF_8).get(1);
    }

    private Path download(URL link) throws IOException {
        Files.createDirectories(tempDir);

        Path destination = Files.createTempFile(tempDir, "", ".tmp");

        ReadableByteChannel rbc = Channels.newChannel(link.openStream());
        FileOutputStream fos = new FileOutputStream(destination.toAbsolutePath().toFile());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        return destination;
    }

    private URL url(String template, String... params) throws MalformedURLException {
        return new URL(String.format(template, params));
    }
}
