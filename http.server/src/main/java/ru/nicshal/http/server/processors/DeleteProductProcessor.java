package ru.nicshal.http.server.processors;

import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class DeleteProductProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output, ProductRepository productRepository) throws IOException {
        String stringID = httpRequest.getParameter("id");
        String response = "";
        if (stringID == null) {
            response = "HTTP/1.1 400 Bad Request\r\nContent-Type: text/html\r\n\r\n";
        } else {
            UUID id = UUID.fromString(stringID);
            if (productRepository.delete(id)) {
                response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + id;
            } else {
                response = "HTTP/1.1 204 No Content\r\nContent-Type: text/html\r\n\r\n";
            }
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}