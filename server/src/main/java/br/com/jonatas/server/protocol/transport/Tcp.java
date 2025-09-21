package br.com.jonatas.server.protocol.transport;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Tcp implements TransportProtocol {

    private final int port;
    private ServerSocket serverSocket;

    public Tcp(int port) throws IOException {
        this.port = port;
    }

    public Tcp(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.port = serverSocket.getLocalPort();
    }

    @Override
    public void open() throws IOException {
        if(this.serverSocket == null || this.serverSocket.isClosed()) {
            this.serverSocket = new ServerSocket(this.port);
        }
    }

    @Override
    public void close() throws IOException {
        if(this.serverSocket != null && !this.isOpen()) {
            this.serverSocket.close();
        }
    }

    @Override
    public boolean isOpen() {
        if(this.serverSocket == null) return false;
        return !this.serverSocket.isClosed();
    }

    @Override
    public Socket acceptConnection() throws IOException {
        if(this.serverSocket == null) return null;
        return this.serverSocket.accept();
    }

    @Override
    public boolean isClose() {
        if(this.serverSocket == null) return true;
        return this.serverSocket.isClosed();
    }

    @Override
    public int getPort() {
        return this.port;
    }

}
