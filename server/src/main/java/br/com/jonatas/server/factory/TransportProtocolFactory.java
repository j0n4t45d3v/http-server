package br.com.jonatas.server.factory;

import br.com.jonatas.server.ServerConfiguration;
import br.com.jonatas.server.protocol.transport.Tcp;
import br.com.jonatas.server.protocol.transport.TransportProtocol;

import java.io.IOException;

public class TransportProtocolFactory {
    public static TransportProtocol create() throws IOException {
        return new Tcp(ServerConfiguration.INSTANCE.getServerPort()) ;
    }
}
