package net.serenitybdd.screenplay.jenkins.tasks.configuration;

public class Axis {

    private String name;
    private String[] values;

    public Axis(String name, String[] values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return this.name;
    }

    public String getValues() {
        String dimensions = "";
        for(String val : values) {
            dimensions += val + " ";
        }

        return dimensions;
    }
}
