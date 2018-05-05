package net.serenitybdd.integration.jenkins.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import hudson.cli.CLI;
import hudson.cli.CLIConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;

public class JenkinsClientExecutor {

    private final URL jenkinsUrl;
    private final List<String> args;

    public JenkinsClientExecutor(URL jenkinsUrl, String... args) {
        this.jenkinsUrl = jenkinsUrl;
        this.args = asList(args);
    }

    public JenkinsClientExecutor call(String... args) {
        return new JenkinsClientExecutor(jenkinsUrl, args);
    }

    public int execute() {
        return execute(System.in, System.out, System.err);
    }

    public int execute(InputStream input, OutputStream output, OutputStream error) {
        try {
            CLI connection = connectTo(jenkinsUrl);

            int result = connection.execute(args, input, output, error);

            connection.close();

            return result;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(String.format("Couldn't connect to Jenkins at '%s'", jenkinsUrl), e);
        }
    }


    private CLI connectTo(URL jenkinsUrl) {
        try {
            return new CLIConnectionFactory()
                    .url(jenkinsUrl)
                    .executorService(jenkinsCLIExecutorService())
                    .connect();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(String.format("Couldn't connect to Jenkins at '%s'", jenkinsUrl), e);
        }
    }

    private ExecutorService jenkinsCLIExecutorService() {
        return Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("jenkins-cli-%d").build());
    }
}
