package ru.nicshal.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String username;
    private boolean isEnable;

    private static int usersCounter = 0;

    public static ClientHandler createClientHandler(Server server, Socket socket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(server, socket);
        clientHandler.run();
        return clientHandler;
    }

    private ClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.generateUsername();
        this.isEnable = true;
    }

    public String getUsername() {
        return username;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateUsername() {
        usersCounter++;
        this.username = "user" + usersCounter;
    }

    private void run() {
        new Thread(() -> {
            try {
                System.out.println("Подключился новый клиент");
                while (isEnable) {
                    String msg = in.readUTF();
                    if (msg.startsWith("/")) {
                        processComand(msg);
                        continue;
                    }
                    server.broadcastMessage(username + ": " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }).start();
    }

    private void processComand(String msg) {
        if (msg.startsWith("/exit")) {
            sayGoodbay();
            disconnect();
            isEnable = false;
        } else if (msg.startsWith("/w ")) {
            String[] arr = msg.split(" ", 3);
            if (arr.length == 3) {
                server.individualMessage(arr[1], arr[2]);
            }
        }
    }

    private void sayGoodbay() {
        sendMessage("stop");
        server.unsubscribe(this);
        server.broadcastMessage("Пользователь " + username + " покинул чат");
    }

    private void disconnect() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}