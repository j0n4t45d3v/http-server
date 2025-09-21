package br.com.jonatas.server;

import br.com.jonatas.server.factory.ServerFactory;

import java.io.IOException;
import java.util.Properties;

public class ServerConfiguration {

    public static final ServerConfiguration INSTANCE;

    static {
        try {
            INSTANCE = new ServerConfiguration();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final Properties configuration;


    private ServerConfiguration() throws IOException {
        Properties defaultValue = new Properties();
        defaultValue.load(this.getClass().getClassLoader().getResourceAsStream("default.properties"));
        this.configuration = new Properties(defaultValue);
        this.configuration.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
    }

    public int getServerPort() {
        return Integer.parseInt(this.configuration.getProperty("httpserver.server.port"));
    }

    public ServerFactory.TypeServer getServerType() {
        return switch (this.configuration.getProperty("httpserver.server.type")) {
            case "http_1_1" -> ServerFactory.TypeServer.HTTP_1_1;
            case "https_1_1" -> ServerFactory.TypeServer.HTTPS_1_1;
            case "websocket" -> ServerFactory.TypeServer.WEBSOCKET;
            case "ftp" -> ServerFactory.TypeServer.FTP;
            default ->
                    throw new RuntimeException("Invalid property 'httpserver.server.type' use <http_1_1|https_1_1|websocket|ftp>");
        };
    }

    public int getServerMaxThreadPool() {
        return Integer.parseInt(this.configuration.getProperty("httpserver.server.max-thread"));
    }

}
