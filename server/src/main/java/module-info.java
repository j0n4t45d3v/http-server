module br.com.jonatas.server {
    requires com.google.gson;
    requires org.slf4j;
    requires br.com.jonatas.annotation;
    requires br.com.jonatas.annotationprocessor;

    exports br.com.jonatas.server;
    exports br.com.jonatas.server.dto.http;
    exports br.com.jonatas.server.enumerate;
    exports br.com.jonatas.server.factory;
    exports br.com.jonatas.server.protocol.application;
    exports br.com.jonatas.server.protocol.transport;
    exports br.com.jonatas.server.connection;
    exports br.com.jonatas.server.config;
}