package br.com.jonatas.annotationprocessor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> getAllAnnotatedController() {
        List<Class<?>> controllers = new ArrayList<>();
        try (InputStream in = ClassScanner.class
                .getClassLoader()
                .getResourceAsStream("META-INF/controllers.txt")) {
            if (in == null) return controllers;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    controllers.add(Class.forName(line));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar controllers anotados", e);
        }
        return controllers;
    }
}
