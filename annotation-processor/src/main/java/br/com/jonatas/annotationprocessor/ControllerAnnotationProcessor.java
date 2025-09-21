package br.com.jonatas.annotationprocessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("br.com.jonatas.annotation.Controller")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ControllerAnnotationProcessor extends AbstractProcessor {

    private final Set<String> controllersClass = new HashSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Filer filer = this.processingEnv.getFiler();
        this.controllersClass.addAll(this.getElementsAnnotated(annotations, roundEnv));

        if(roundEnv.processingOver() && !controllersClass.isEmpty()) {
            this.getResourceFileController(filer)
                    .ifPresent(resource -> this.writeControllerClassInResource(resource, this.controllersClass));
        }
        return true;
    }

    private Set<String> getElementsAnnotated(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<String> elements = new HashSet<>();
        for (TypeElement typeElement : annotations) {

            elements.addAll(roundEnv.getElementsAnnotatedWith(typeElement)
                    .stream()
                    .map(element -> {
                        String controllerFullPackage = ((TypeElement) element).getQualifiedName().toString();
                        this.processingEnv
                                .getMessager()
                                .printMessage(Diagnostic.Kind.WARNING, "Classe encontrada: " + controllerFullPackage);
                        return controllerFullPackage;
                    })
                    .collect(Collectors.toUnmodifiableSet()));
        }
        return elements;

    }

    private Optional<FileObject> getResourceFileController(Filer filer) {
        try {
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/controllers.txt");
            return Optional.of(resource);
        } catch (IOException e) {
            this.processingEnv
                    .getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, e.toString());
            return Optional.empty();
        }
    }

    private void writeControllerClassInResource(FileObject resource, Set<String> elements) {
        try (Writer resourceWriter = resource.openWriter()) {
            for (String clazz : elements) {
                resourceWriter.append(clazz).append("\n");
            }
        } catch (IOException e) {
            this.processingEnv
                    .getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }
}
