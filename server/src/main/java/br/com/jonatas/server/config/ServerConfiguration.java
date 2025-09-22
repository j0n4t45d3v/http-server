package br.com.jonatas.server.config;

import br.com.jonatas.server.enumerate.TypeServer;

import java.io.IOException;
import java.io.InputStream;
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
        ClassLoader classLoader = this.getClass().getClassLoader();
        defaultValue.load(classLoader.getResourceAsStream("default.properties"));
        this.configuration = new Properties(defaultValue);
        try(InputStream applicationConfiguration = classLoader.getResourceAsStream("application.properties")) {
            if(applicationConfiguration != null) {
                this.configuration.load(applicationConfiguration);
            }
        }
    }

    public int getServerPort() {
        return Integer.parseInt(this.configuration.getProperty("httpserver.server.port"));
    }

    public TypeServer getServerType() {
        return switch (this.configuration.getProperty("httpserver.server.type")) {
            case "http_1_1" -> TypeServer.HTTP_1_1;
            case "https_1_1" -> TypeServer.HTTPS_1_1;
            case "websocket" -> TypeServer.WEBSOCKET;
            case "ftp" -> TypeServer.FTP;
            default ->
                    throw new RuntimeException("Invalid property 'httpserver.server.type' use <http_1_1|https_1_1|websocket|ftp>");
        };
    }

    public int getServerMaxThreadPool() {
        return Integer.parseInt(this.configuration.getProperty("httpserver.server.max-thread"));
    }

}
