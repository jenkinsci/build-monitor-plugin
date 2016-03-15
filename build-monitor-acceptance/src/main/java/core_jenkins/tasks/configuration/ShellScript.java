package core_jenkins.tasks.configuration;

public enum ShellScript {
    Finishes_with_Success("exit 0;"),
    Finishes_with_Error("exit 1;");

    ShellScript(String script) {
        this.script = script;
    }

    @Override
    public String toString() {
        return this.name().replaceAll("_", " ");
    }

    public String code() {
        return script;
    }

    private final String script;
}
