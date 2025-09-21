package br.com.jonatas.server.connection;

import br.com.jonatas.server.dto.ConnectionRequest;
import br.com.jonatas.server.dto.http.Response;
import br.com.jonatas.server.router.RouteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpConnectionResolver implements ConnectionResolver {

    private static final Logger log = LoggerFactory.getLogger(HttpConnectionResolver.class);

    private final RouteManager routeManager;
    private final ExecutorService executorService;

    public HttpConnectionResolver(RouteManager routeManager) {
        this.routeManager = routeManager;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void execute(Socket socket) {
        executorService.execute(() -> {
            PrintWriter out = null;
            try (socket; Scanner in = new Scanner(socket.getInputStream())) {
                out = new PrintWriter(socket.getOutputStream());
                var headers = ConnectionRequest.of(in);
                headers.add("Accept", "application/json");

                log.info("{} {}", headers.method(), headers.route());
                Response response = this.routeManager.executeRoute(headers)
                        .addHeader("Connection", "close")
                        .addHeader("Server", "HttpServerCustom/1.0")
                        .addHeader("Date", LocalDateTime.now()
                                .atZone(ZoneId.of("GMT"))
                                .format(DateTimeFormatter.RFC_1123_DATE_TIME));

                out.printf("HTTP/1.1 %d%n", response.getStatus());

                for (Map.Entry<String, String> header : response.getHeaders()) {
                    out.println(header.getKey().trim() + ": " + header.getValue().trim());
                }
                out.println();
                String body = response.getBody();
                if (body != null && !body.isEmpty()) {
                    out.println(body);
                }
                out.flush();

            } catch (Exception e) {
                log.error("Error Resolve Request", e);
                if (out != null) {
                    out.print("HTTP/1.1 500");
                    out.println("Connection: close");
                    out.flush();
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        });
    }


}
