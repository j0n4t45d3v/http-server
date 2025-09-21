package br.com.jonatas.controller;

import br.com.jonatas.annotation.Controller;
import br.com.jonatas.annotation.GetRoute;
import br.com.jonatas.annotation.Route;
import br.com.jonatas.server.dto.http.Response;
import br.com.jonatas.server.enumerate.ContentType;

@Controller
@Route("/html")
public class HtmlController {

    @GetRoute
    public void getMethod(Response response) {
        response.setContentType(ContentType.HTML);
        response.writeBody("<h1>Html Controller</h1>");
    }
}
