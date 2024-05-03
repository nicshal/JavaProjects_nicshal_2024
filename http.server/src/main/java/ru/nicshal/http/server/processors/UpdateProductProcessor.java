package ru.nicshal.http.server.processors;

import com.google.gson.Gson;
import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.data.Item;
import ru.nicshal.http.server.data.Storage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UpdateProductProcessor  implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        String response = "";
        if (Storage.update(item)) {
            String jsonOutItem = gson.toJson(item);
            response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + jsonOutItem;
        } else {
            response = "HTTP/1.1 204 No Content\r\nContent-Type: text/html\r\n\r\n";
        }

        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}