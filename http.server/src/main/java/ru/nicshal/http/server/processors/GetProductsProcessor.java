package ru.nicshal.http.server.processors;

import com.google.gson.Gson;
import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.data.Item;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class GetProductsProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output, ProductRepository productRepository) throws IOException {
        String stringID = httpRequest.getParameter("id");
        List<Item> items;
        if (stringID == null) {
            items = productRepository.getItems();
        } else {
            UUID id = UUID.fromString(stringID);
            items = productRepository.getItemByID(id);
        }
        Gson gson = new Gson();
        String result = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + gson.toJson(items);
        output.write(result.getBytes(StandardCharsets.UTF_8));
    }

}