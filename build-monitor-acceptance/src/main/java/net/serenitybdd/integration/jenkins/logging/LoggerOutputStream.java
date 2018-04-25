package net.serenitybdd.integration.jenkins.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class LoggerOutputStream extends OutputStream {
    private Witness witness;
    private String buffer;

    public LoggerOutputStream(Witness witness) {
        this.witness = witness;
        buffer = "";
    }

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[1];
        bytes[0] = (byte) (b & 0xff);
        buffer = buffer + new String(bytes, Charset.forName("UTF-8"));

        if (buffer.endsWith("\n")) {
            buffer = buffer.substring(0, buffer.length() - 1);
            flush();
        }
    }

    @Override
    public void flush() {
        witness.note(buffer);
        buffer = "";
    }
}
