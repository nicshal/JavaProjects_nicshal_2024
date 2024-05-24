package ru.nicshal.http.server.processors;

import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {

    void execute(HttpRequest httpRequest, OutputStream output, ProductRepository productRepository) throws IOException;

}