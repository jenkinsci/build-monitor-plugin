package net.serenitybdd.integration.jenkins.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

class JenkinsLogLineWatcher {
    private final Pattern pattern;
    private final Deferred<Matcher, ?, ?> deferred = new DeferredObject();

    public JenkinsLogLineWatcher(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public boolean matches(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            deferred.resolve(matcher);

            return true;
        }

        return false;
    }

    public Promise<Matcher, ?, ?> promise() {
        return deferred.promise();
    }
}
