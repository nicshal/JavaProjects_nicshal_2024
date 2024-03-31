package ru.nicshal.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final int port;
    private final Map<String, ClientHandler> clients;
    private final Object monitor;

    public Server(int port) {
        this.port = port;
        this.clients = new HashMap<>();
        this.monitor = this;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Сервер запущен на порту: %d, ожидаем подключения клиентов\n", port);
            while (true) {
                Socket socket = serverSocket.accept();
                subscribe(ClientHandler.createClientHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        synchronized (monitor) {
            clients.put(clientHandler.getUsername(), clientHandler);
        }
    }

    public void unsubscribe(ClientHandler clientHandler) {
        synchronized (monitor) {
            clients.remove(clientHandler.getUsername());
        }
    }

    public void broadcastMessage(String message) {
        synchronized (monitor) {
            for (ClientHandler c : clients.values()) {
                c.sendMessage(message);
            }
        }
    }

    public void individualMessage(String client, String message) {
        synchronized (monitor) {
            if (clients.containsKey(client)) {
                clients.get(client).sendMessage(message);
            }
        }
    }

}