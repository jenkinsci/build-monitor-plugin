package net.serenitybdd.integration.jenkins.logging;

public interface Witness {
    void note(String msg);
    void note(String format, Object arg);
    void note(String format, Object arg1, Object arg2);
    void note(String format, Object... arguments);
    void note(String msg, Throwable t);
}
