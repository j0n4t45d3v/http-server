package br.com.jonatas.server.dto.http;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Optional;

public class Request {

    private final String body;
    private final Map<String, ?> params;
    private final Map<String, String> headers;

    public Request(
            String body,
            Map<String, ?> params,
            Map<String, String> headers
    ) {
        this.body = body;
        this.params = params;
        this.headers = headers;
    }

    public String getBodyRaw() {
        return this.body;
    }

    public <T> Optional<T> getBody(Class<T> type) {
        if (this.body == null || this.body.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Gson().fromJson(this.body, type));
    }

    public <R> Optional<R> getParameter(String name, Class<R> type) {
        if (name == null) {
            return Optional.empty();
        }
        Object paramValue = this.params.get(name);
        if (type.isInstance(paramValue)) {
            return Optional.of(type.cast(paramValue));
        }
        System.out.println("Value is not of type " + type.getSimpleName());
        return Optional.empty();
    }

    public Optional<String> getHeader(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.headers.get(name));
    }

}
