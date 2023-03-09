package net.serenitybdd.screenplay.jenkins;

import java.util.UUID;
import net.serenitybdd.screenplay.Actor;

public class JenkinsUser extends Actor {
    public static JenkinsUser named(String username) {
        return new JenkinsUser(username, UUID.randomUUID().toString());
    }

    private final String password;

    public JenkinsUser(String name, String password) {
        super(name);
        this.password = password;
    }

    public String password() {
        return password;
    }
}
