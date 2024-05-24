package ru.nicshal.http.server.processors;

import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.repositories.ProductRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static ru.nicshal.http.server.utils.ApplicationConst.PATH_NAME;

public class GetFileProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, OutputStream output, ProductRepository productRepository) throws IOException {
        String fileName = httpRequest.getParameter("name");
        String response = "";
        if (fileName == null) {
            response = "HTTP/1.1 400 Bad Request\r\nContent-Type: text/html\r\n\r\n";
        } else {
            File file = new File(PATH_NAME + "/" + fileName);
            if (file.exists()) {
                StringBuilder buffer = new StringBuilder("HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=utf-8\r\n\r\n");
                String rec;
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                    while ((rec = bufferedReader.readLine()) != null) {
                        buffer.append(rec);
                    }
                    response = buffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                response = "HTTP/1.1 404 Not Found\r\nContent-Type: text/html\r\n\r\n";
            }
        }
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}