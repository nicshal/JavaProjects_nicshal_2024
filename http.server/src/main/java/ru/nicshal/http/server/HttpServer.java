package ru.nicshal.http.server;

import ru.nicshal.http.server.handlers.HttpRequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private final int port;
    private final Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            System.out.println("Диспетчер проинициализирован");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    HttpRequestHandler.executeHttpRequestHandler(this, socket);
                } catch (Exception e) {
                    System.out.println("Возникла ошибка при обработке нового подключения");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

}