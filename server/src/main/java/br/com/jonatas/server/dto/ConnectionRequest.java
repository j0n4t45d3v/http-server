package br.com.jonatas.server.dto;

import java.util.*;

public record ConnectionRequest(
        String method,
        String route,
        Map<String, String> headers
) {

    public static ConnectionRequest of(Scanner input) {
        String line;
        boolean firstLine = true;
        ConnectionRequest headerReceived = null;
        while(input.hasNextLine()) {
            line = input.nextLine();
            if(firstLine) {
                String[] splitLine = line.split(" ");
                String path  = (splitLine[1] + "/").replaceAll("/{2,}", "/");
                headerReceived = new ConnectionRequest(splitLine[0], path, new HashMap<>());
                firstLine = false;
                continue;
            }
            if(line.isEmpty()) break;
            headerReceived.add(line);
        }

        return headerReceived;
    }

    public Optional<String> getHeader(String header) {
        if (header == null || header.isEmpty()) return Optional.empty();
        return Optional.ofNullable(this.headers.get(header));
    }

    public void add(String header) {
        var keyValue = header.split(":");
        this.add(keyValue[0], keyValue[1]);
    }

    public void add(String key, String value) {
        this.headers.put(key, value);
    }

    public Set<Map.Entry<String, String>> entrySetHeaders() {
        return this.headers.entrySet();
    }
}
