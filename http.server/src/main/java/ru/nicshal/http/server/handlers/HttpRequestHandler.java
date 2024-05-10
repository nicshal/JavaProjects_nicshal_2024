package ru.nicshal.http.server.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.HttpServer;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandler implements Runnable {

    private final Logger logger = LogManager.getLogger(HttpRequestHandler.class.getName());
    private final HttpServer server;
    private final Socket socket;

    public HttpRequestHandler(HttpServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream();
             this.socket;) {
            logger.info("Новое подключение");
            byte[] buffer = new byte[8192];
            int n = in.read(buffer);
            if (n > 0) {
                String rawRequest = new String(buffer, 0, n);
                HttpRequest request = new HttpRequest(rawRequest);
                request.info(true);
                server.getDispatcher().execute(request, out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}