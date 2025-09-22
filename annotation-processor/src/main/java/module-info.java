module br.com.jonatas.annotationprocessor {
    requires java.compiler;
    exports br.com.jonatas.annotationprocessor to br.com.jonatas.server;
    exports br.com.jonatas.annotationprocessor.util to br.com.jonatas.server;
}