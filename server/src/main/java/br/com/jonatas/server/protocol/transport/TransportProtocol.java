package br.com.jonatas.server.protocol.transport;

import java.io.IOException;
import java.net.Socket;

public interface TransportProtocol extends AutoCloseable {
    void open() throws IOException;
    boolean isOpen();
    Socket acceptConnection() throws IOException;
    boolean isClose();
    int getPort();
}
