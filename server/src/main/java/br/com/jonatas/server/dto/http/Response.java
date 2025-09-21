package br.com.jonatas.server.dto.http;

import br.com.jonatas.server.enumerate.ContentType;
import com.google.gson.Gson;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Response {

    private String body;
    private int status;
    private final Map<String, String> headers;

    public Response(Map<String, String> headers) {
        this.headers = headers;
        this.status = 200;
        this.setContentType(ContentType.HTML);
    }

    public <T> Response writeBodyJson(T body) {
        this.body = new Gson().toJson(body);
        return this.setContentType(ContentType.JSON);
    }

    public Response writeBody(String body) {
        this.body = body;
        return this;
    }

    public Response setContentType(ContentType type) {
        this.addHeader("Content-Type", type.getValue());
        return this;
    }

    public Response addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Optional<String> getHeader(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.headers.get(name));
    }

    public String getBody() {
        return this.body;
    }

    public Set<Map.Entry<String, String>> getHeaders() {
        return this.headers.entrySet();
    }

    public int getStatus() {
        return status;
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

}
