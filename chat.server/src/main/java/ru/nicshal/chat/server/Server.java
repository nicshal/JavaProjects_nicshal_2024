package ru.nicshal.chat.server;

import ru.nicshal.chat.server.authentication.AuthenticationService;
import ru.nicshal.chat.server.authentication.DatabaseAuthenticationService;
//import ru.nicshal.chat.server.authentication.InMemoryAuthenticationService;
import ru.nicshal.chat.server.handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final int port;
    private final Map<String, ClientHandler> clients;
    private final AuthenticationService authenticationService;
    private final Object monitor;

    public Server(int port) {
        this.port = port;
        this.clients = new HashMap<>();
        this.monitor = this;
        //this.authenticationService = new InMemoryAuthenticationService();
        this.authenticationService = new DatabaseAuthenticationService();
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.printf("Сервер запущен на порту: %d, ожидаем подключения клиентов\n", port);
            System.out.println("Сервис аутентификации запущен: " + authenticationService.getClass().getSimpleName());
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ClientHandler.createClientHandler(this, socket);
                } catch (Exception e) {
                    System.out.println("Возникла ошибка при обработке подключившегося клиента");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(ClientHandler clientHandler) {
        synchronized (monitor) {
            broadcastMessage("К чату присоединился " + clientHandler.getNickname());
            clients.put(clientHandler.getNickname(), clientHandler);
        }
    }

    public void unsubscribe(ClientHandler clientHandler) {
        synchronized (monitor) {
            clients.remove(clientHandler.getNickname());
            broadcastMessage("Из чата вышел " + clientHandler.getNickname());
        }
    }

    public void disconnectClient(String nickname) {
        synchronized (monitor) {
            if (clients.containsKey(nickname)) {
                clients.get(nickname).setEnable(false);
                individualMessage(nickname, "Вы будете отключены от чата");
                individualMessage(nickname, "stop");
                clients.remove(nickname);
                broadcastMessage("От чата отключен " + nickname);
            }
        }
    }

    public void broadcastMessage(String message) {
        synchronized (monitor) {
            for (ClientHandler c : clients.values()) {
                c.sendMessage(message);
            }
        }
    }

    public void individualMessage(String nickname, String message) {
        synchronized (monitor) {
            if (clients.containsKey(nickname)) {
                clients.get(nickname).sendMessage(message);
            }
        }
    }

    public boolean isNicknameActive(String nickname) {
        synchronized (monitor) {
            return clients.containsKey(nickname);
        }
    }

}