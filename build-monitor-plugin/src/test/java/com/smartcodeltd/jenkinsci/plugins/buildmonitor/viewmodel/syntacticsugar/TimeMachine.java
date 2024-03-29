package com.smartcodeltd.jenkinsci.plugins.buildmonitor.viewmodel.syntacticsugar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jan Molak
 */
public class TimeMachine {
    private Date systemTime;

    public static TimeMachine currentTime() {
        return new TimeMachine(mock(Date.class));
    }

    public static TimeMachine assumeThat(Date currentTime) {
        return new TimeMachine(currentTime);
    }

    public Date is(String currentTime) throws ParseException {
        Date currentDate = new SimpleDateFormat("H:m:s").parse(currentTime);

        when(this.systemTime.getTime()).thenReturn(currentDate.getTime());

        return this.systemTime;
    }

    private TimeMachine(Date currentTime) {
        this.systemTime = currentTime;
    }
}
