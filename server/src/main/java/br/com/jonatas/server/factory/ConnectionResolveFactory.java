package br.com.jonatas.server.factory;

import br.com.jonatas.server.config.ServerConfiguration;
import br.com.jonatas.server.connection.ConnectionResolver;
import br.com.jonatas.server.connection.HttpConnectionResolver;
import br.com.jonatas.server.router.RouteManager;

class ConnectionResolveFactory {
    public static ConnectionResolver createHttpResolver() {
        return new HttpConnectionResolver(RouteManager.INSTANCE, ServerConfiguration.INSTANCE);
    }
}
