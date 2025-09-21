package br.com.jonatas.annotation.util;

import br.com.jonatas.annotation.GetRoute;
import br.com.jonatas.annotation.PostRoute;
import br.com.jonatas.annotation.Route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils {

    public record RouteMethod(String path, String httpMethod, Method callableMethod) {
    }

    public static List<RouteMethod> getAllMethodAnnotatedInController(Class<?> controller) {
        List<RouteMethod> methods = new ArrayList<>();

        String path = null;
        String httpMethod = "GET";
        for (Method method : controller.getMethods()) {
            if (method.isAnnotationPresent(GetRoute.class)) {
                GetRoute getRoute = method.getAnnotation(GetRoute.class);
                path = getRoute.value();
                httpMethod = "GET";
            } else if (method.isAnnotationPresent(PostRoute.class)) {
                PostRoute postRoute = method.getAnnotation(PostRoute.class);
                path = postRoute.value();
                httpMethod = "POST";
            } else {
                continue;
            }

            methods.add(new RouteMethod(path, httpMethod, method));
            path = null;
        }
        return methods;
    }

    public static String getControllerRoute(Class<?> controller) {
        if (controller.isAnnotationPresent(Route.class)) {
            return controller.getAnnotation(Route.class).value();
        }
        return "/";
    }
}
