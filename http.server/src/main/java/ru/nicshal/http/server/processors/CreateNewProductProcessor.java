package ru.nicshal.http.server.processors;

import com.google.gson.Gson;
import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.data.Item;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CreateNewProductProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output, ProductRepository productRepository) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        if (item.getId() == null) {
            item.setId(UUID.randomUUID());
        }
        productRepository.save(item);
        String jsonOutItem = gson.toJson(item);

        String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + jsonOutItem;
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}