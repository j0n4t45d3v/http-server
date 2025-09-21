package br.com.jonatas.server.util;

import br.com.jonatas.annotation.util.AnnotationUtils;

public class PathBuilder {

    public static String toUri(Class<?> controllerClazz, AnnotationUtils.RouteMethod subRoute) {
        return String.format("/%s/%s/", AnnotationUtils.getControllerRoute(controllerClazz), subRoute.path())
                .replaceAll("/{2,}", "/");
    }
}
