package br.com.jonatas.server.protocol.application;

import java.io.IOException;

public interface Server extends AutoCloseable {
    void start() throws IOException;
    void stop() throws Exception;
}
