package net.serenitybdd.integration.jenkins.environment.rules;

import net.serenitybdd.integration.jenkins.JenkinsInstance;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class FindFreePort implements ApplicativeTestRule<JenkinsInstance> {
    private static final Logger Log = LoggerFactory.getLogger(FindFreePort.class);

    private static final int Dynamic_Range_Start = 49152;
    private static final int Dynamic_Range_End   = 65535;

    private final int rangeStart;
    private final int rangeEnd;

    public static FindFreePort useFreePortFromDynamicRange() {
        return new FindFreePort(Dynamic_Range_Start, Dynamic_Range_End);
    }

    public static FindFreePort useFreePortFromTheFollowingRange(int rangeStart, int rangeEnd) {
        return new FindFreePort(rangeStart, rangeEnd);
    }

    public FindFreePort(int rangeStart, int rangeEnd) {
        checkArgument(rangeStart <= rangeEnd, format("Start of the port range (%d) should be lower than the end of the range (%d)", rangeStart, rangeEnd));

        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    @Override
    public TestRule applyTo(final JenkinsInstance instance) {
        return new TestWatcher() {
            @Override
            protected void starting(Description description) {
                int portNumber = anyFreeLocalPortInRange(rangeStart, rangeEnd);

                Log.info("Found a free port: {}", portNumber);

                instance.setPort(portNumber);
            }
        };
    }

    private int anyFreeLocalPortInRange(int from, int to){
        while(true){
            int candidate = (int) ((Math.random() * (to - from)) + from);
            if(isFree(candidate)){
                return candidate;
            }
        }
    }

    private boolean isFree(int port){
        try {
            ServerSocket ss = new ServerSocket(port);
            ss.close();

            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
}
