package br.com.jonatas.server.router;

import br.com.jonatas.server.dto.ConnectionRequest;
import br.com.jonatas.server.dto.http.Request;
import br.com.jonatas.server.dto.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RouteManager {

    private static final Logger log = LoggerFactory.getLogger(RouteManager.class);
    public static final RouteManager INSTANCE = new RouteManager(RouteRegister.INSTANCE);

    private final RouteRegister routeRegister;

    public RouteManager(RouteRegister routeRegister) {
        this.routeRegister = routeRegister;
    }

    public Response executeRoute(ConnectionRequest connectionRequest) {
        Map<String, RouteRegister.CallRoute> controller = this.routeRegister.getRouteMethods(connectionRequest.route());
        Optional<String> accept = connectionRequest.getHeader("Accept");
        boolean acceptJson = accept.isPresent() && accept.get().equals("application/json");
        Response response = new Response(new HashMap<>());
        if (controller == null) {
            response.setStatus(404);
            if (acceptJson) {
                return response.writeBodyJson(Map.of(
                        "reason", "resource_not_found",
                        "message", String.format("Path '%s' not found", connectionRequest.route()),
                        "timestamp", LocalDateTime.now().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME)
                ));
            }
            return response
                    .writeBody("<h1>Not Found</h1>");
        }
        RouteRegister.CallRoute callable = controller.get(connectionRequest.method());
        if (callable == null) {
            response.setStatus(405);
            if (acceptJson) {
                return response.writeBodyJson(Map.of(
                        "reason", "method_not_allowed",
                        "message", String.format("Http method '%s' not allowed", connectionRequest.method()),
                        "timestamp", LocalDateTime.now().atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME)
                ));
            }
            return response.writeBody("<h1>Method Not Allowed</h1>");
        }
        Map<String, String> headers = new HashMap<>();
        Map<String, ?> params = new HashMap<>();
        Request request = new Request(null, params, headers);
        connectionRequest.entrySetHeaders().forEach(header -> {
            headers.put(header.getKey(), header.getValue());
        });
        try {
            callable.execute(request, response);
        } catch (Exception e) {
            response
                    .setStatus(500)
                    .writeBody("<h1>Internal Server Error</h1>");
            log.error("Error {}", e.getMessage(), e);
        }
        return response;
    }
}
