package br.com.jonatas.server;

import br.com.jonatas.server.factory.ServerFactory;
import br.com.jonatas.server.protocol.application.Server;

import static br.com.jonatas.server.factory.ServerFactory.create;

public class ServerApplicationRunner {

    public static void start() {
        try(Server server = create(ServerFactory.TypeServer.HTTP_1_1)) {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
