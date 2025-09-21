package br.com.jonatas.server.protocol.application;

import br.com.jonatas.server.connection.ConnectionResolver;
import br.com.jonatas.server.protocol.transport.TransportProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Http implements Server {

    private static final Logger log = LoggerFactory.getLogger(Http.class);

    private final TransportProtocol transportProtocol;
    private final ConnectionResolver connectionResolver;

    public Http(
            TransportProtocol transportProtocol,
            ConnectionResolver connectionResolver
    ) {
        this.transportProtocol = transportProtocol;
        this.connectionResolver = connectionResolver;
    }

    @Override
    public void start() throws IOException {
        this.transportProtocol.open();
        log.info("Server Started http://localhost:{}", this.transportProtocol.getPort());
        while(this.transportProtocol.isOpen()) {
            try {
                this.connectionResolver.execute(this.transportProtocol.acceptConnection());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.stop();
    }

    @Override
    public void stop() throws Exception {
        this.transportProtocol.close();
    }

}
