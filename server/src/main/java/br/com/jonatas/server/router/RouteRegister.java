package br.com.jonatas.server.router;

import br.com.jonatas.annotation.Route;
import br.com.jonatas.annotation.util.AnnotationUtils;
import br.com.jonatas.annotationprocessor.util.ClassScanner;
import br.com.jonatas.server.dto.http.Request;
import br.com.jonatas.server.dto.http.Response;
import br.com.jonatas.server.util.PathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteRegister {

    private static final Logger log = LoggerFactory.getLogger(RouteRegister.class);
    public static final RouteRegister INSTANCE = new RouteRegister();

    @FunctionalInterface
    public interface CallRoute {
        void execute(Request request, Response response) throws InvocationTargetException, IllegalAccessException;
    }

    private final Map<String, Map<String, CallRoute>> controllers = new HashMap<>();

    {
        ClassScanner.getAllAnnotatedController().forEach(controller -> {
            try {
                Object instance = controller.getConstructor().newInstance();
                List<AnnotationUtils.RouteMethod> routes = AnnotationUtils.getAllMethodAnnotatedInController(controller);
                Map<String, CallRoute> controllerMethodRegister;
                for (AnnotationUtils.RouteMethod route : routes) {
                    String routePath = PathBuilder.toUri(controller, route);
                    controllerMethodRegister = this.controllers.computeIfAbsent(routePath, k -> new HashMap<>());
                    if (controllerMethodRegister.get(route.httpMethod()) != null) {
                        log.error("Method '{}' duplicated in route '{}'", route.httpMethod(), routePath);
                        break;
                    }
                    controllerMethodRegister.put(route.httpMethod(), this.makeCallableRoute(instance, route));

                }
            } catch (Exception e) {
                log.error("Failed to registry controller {}", controller.getSimpleName(), e);
            }
        });
    }

    private CallRoute makeCallableRoute(final Object instance, final AnnotationUtils.RouteMethod route) {
        return (request, response) -> {
            Method method = route.callableMethod();
            switch (method.getParameterCount()) {
                case 0 -> method.invoke(instance);
                case 1 -> {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    if (parameterType.isInstance(request)) {
                        method.invoke(instance, request);
                    } else if (parameterType.isInstance(response)) {
                        method.invoke(instance, response);
                    }
                }
                case 2 -> {
                    Class<?> firstParameterType = method.getParameterTypes()[0];
                    Class<?> secondParameterType = method.getParameterTypes()[1];
                    if (firstParameterType.isInstance(request) && secondParameterType.isInstance(response)) {
                        method.invoke(instance, request, response);
                    } else if (firstParameterType.isInstance(response) && secondParameterType.isInstance(request)) {
                        method.invoke(instance, response, request);
                    }
                }
            }
        };
    }

    public Map<String, CallRoute> getRouteMethods(String uri) {
        return this.controllers.get(uri);
    }

}
