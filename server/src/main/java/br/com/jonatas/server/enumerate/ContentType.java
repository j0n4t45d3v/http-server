package br.com.jonatas.server.enumerate;

public enum ContentType {

    TEXT("text/plain"),
    HTML("text/html"),
    XML("application/xml"),
    JSON("application/json");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
