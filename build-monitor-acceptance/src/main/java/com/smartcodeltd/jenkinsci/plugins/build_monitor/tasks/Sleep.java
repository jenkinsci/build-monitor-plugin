package com.smartcodeltd.jenkinsci.plugins.build_monitor.tasks;

import java.util.concurrent.TimeUnit;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;

public final class Sleep {
    public static Performable of(long duration, TimeUnit unit) {
        return Task.where(
                "Sleeps for " + duration + " " + unit.toString().toLowerCase(),
                unused -> {
                    try {
                        long millis = TimeUnit.MILLISECONDS.convert(duration, unit);
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
    }
}
