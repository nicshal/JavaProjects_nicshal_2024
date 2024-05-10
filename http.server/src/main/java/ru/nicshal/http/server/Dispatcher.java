package ru.nicshal.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nicshal.http.server.processors.*;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {

    private final Logger logger = LogManager.getLogger(Dispatcher.class.getName());
    private final Map<String, RequestProcessor> router;
    private final RequestProcessor unknownOperationRequestProcessor;

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("GET /calc", new CalculatorRequestProcessor());
        this.router.put("GET /hello", new HelloWorldRequestProcessor());
        this.router.put("GET /file", new GetFileProcessor());
        this.router.put("GET /items", new GetProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());
        this.router.put("PUT /items", new UpdateProductProcessor());
        this.router.put("DELETE /items", new DeleteProductProcessor());
        this.unknownOperationRequestProcessor = new UnknownOperationRequestProcessor();
        logger.info("Диспетчер проинициализирован");
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream, ProductRepository productRepository) throws IOException {
        if (!router.containsKey(httpRequest.getRouteKey())) {
            unknownOperationRequestProcessor.execute(httpRequest, outputStream, productRepository);
            return;
        }
        router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream, productRepository);
    }

}