package br.com.jonatas.server.factory;

import br.com.jonatas.server.config.ServerConfiguration;
import br.com.jonatas.server.protocol.application.Server;
import br.com.jonatas.server.protocol.application.Http;
import br.com.jonatas.server.connection.HttpConnectionResolver;
import br.com.jonatas.server.router.RouteManager;

import java.io.IOException;
import java.util.Map;

public class ServerFactory {

    public enum TypeServer {
        HTTP_1_1, HTTPS_1_1, WEBSOCKET, FTP;
    };

    @FunctionalInterface
    interface MakeServer {
        Server newInstance() throws IOException;
    }

    private static final Map<TypeServer, MakeServer> servers = Map.of(
            TypeServer.HTTP_1_1, () -> new Http(TransportProtocolFactory.create(), new HttpConnectionResolver(RouteManager.INSTANCE, ServerConfiguration.INSTANCE)),
            TypeServer.HTTPS_1_1, () -> {throw new RuntimeException("Unimplemented HTTPS Server");},
            TypeServer.WEBSOCKET, () -> {throw new RuntimeException("Unimplemented WEBSOCKET Server");},
            TypeServer.FTP, () -> {throw new RuntimeException("Unimplemented FTP Server");}
    );

    public static Server create(TypeServer typeServer) throws IOException {
        return servers.get(typeServer).newInstance();
    }

}
