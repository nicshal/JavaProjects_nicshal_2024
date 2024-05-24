package ru.nicshal.http.server.processors;

import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloWorldRequestProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output, ProductRepository productRepository) throws IOException {
        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Hello World!!!</h1></body></html>";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}