package br.com.jonatas.controller;


import br.com.jonatas.annotation.Controller;
import br.com.jonatas.annotation.GetRoute;
import br.com.jonatas.annotation.PostRoute;
import br.com.jonatas.annotation.Route;
import br.com.jonatas.server.dto.http.Response;

import java.util.Map;

@Controller
@Route("/json")
public class JsonController {

    @GetRoute
    public void getMethod(Response response) {
        response.writeBodyJson(Map.of("message", "success"));
    }

    @GetRoute("/test")
    public void getMethodTest(Response response) {
        response.writeBodyJson(Map.of("sengunda", "rota"));
    }

    @GetRoute("/test2")
    public void getMethodTest2(Response response) {
        response.writeBodyJson(Map.of("sengunda", "rota"));
    }

    @PostRoute("/test/register")
    public void postMethodTest(Response response) {
        response.writeBodyJson(Map.of("post", "rota"));
    }

}
