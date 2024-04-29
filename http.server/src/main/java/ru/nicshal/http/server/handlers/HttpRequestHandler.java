package ru.nicshal.http.server.handlers;

import ru.nicshal.http.server.HttpRequest;
import ru.nicshal.http.server.HttpServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpRequestHandler implements Runnable {

    private final HttpServer server;
    private final Socket socket;

    private static final int THREAD_COUNT = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    public static void executeHttpRequestHandler(HttpServer server, Socket socket) throws IOException {
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(server, socket);
        executorService.execute(httpRequestHandler);
    }

    public HttpRequestHandler(HttpServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream();
             this.socket;) {
            System.out.println("Новое подключение");
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