package br.com.jonatas.server;

import br.com.jonatas.server.factory.ServerFactory;
import br.com.jonatas.server.protocol.application.Server;

import static br.com.jonatas.server.factory.ServerFactory.create;

public class ServerApplicationRunner {

    public static void start() {
        ServerApplicationRunner.start(ServerConfiguration.INSTANCE.getServerType());
    }
    public static void start(ServerFactory.TypeServer typeServer) {
        try(Server server = create(typeServer)) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
