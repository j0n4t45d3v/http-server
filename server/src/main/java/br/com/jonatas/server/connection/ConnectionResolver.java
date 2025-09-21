package br.com.jonatas.server.connection;

import java.net.Socket;

public interface ConnectionResolver {
    void execute(Socket socket);
}
