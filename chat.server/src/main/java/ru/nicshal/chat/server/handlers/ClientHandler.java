package ru.nicshal.chat.server.handlers;

import ru.nicshal.chat.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler {

    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nickname;
    private boolean isEnable;

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
        this.isEnable = true;
    }

    public String getNickname() {
        return nickname;
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

    private void run() {
        new Thread(() -> {
            try (this.socket; this.in; this.out;) {
                System.out.println("Подключился новый клиент");
                if (tryToAuthenticate()) {
                    communicate();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void communicate() throws IOException {
        while (isEnable) {
            String msg = in.readUTF();
            if (server.isNicknameActive(nickname)) {
                if (msg.startsWith("/")) {
                    processComand(msg);
                    continue;
                }
                server.broadcastMessage(nickname + ": " + msg);
            }
        }
    }

    private void processComand(String msg) {
        if (msg.startsWith("/exit")) {
            sayGoodbay();
        } else if (msg.startsWith("/w ")) {
            String[] arr = msg.split(" ", 3);
            if (arr.length == 3) {
                server.individualMessage(arr[1], arr[2]);
            }
        } else if (msg.startsWith("/kick ")) {
            if (server.getAuthenticationService().isAdmin(nickname)) {
                String[] arr = msg.split(" ");
                if (arr.length == 2) {
                    server.disconnectClient(arr[1]);
                } else {
                    sendMessage("Некорректный формат запроса");
                }
            } else {
                sendMessage(nickname + ", команда /kick доступна только администратору чата");
            }
        }
    }

    private void sayGoodbay() {
        sendMessage("stop");
        server.unsubscribe(this);
        isEnable = false;
    }

    private boolean tryToAuthenticate() throws IOException {
        boolean isAuthenticated = false;
        while (!isAuthenticated) {
            String msg = in.readUTF();
            if (msg.startsWith("/auth ")) {
                // /auth login pass
                Optional<String> item = getAuthNickname(msg);
                if (item.isPresent()) {
                    this.nickname = item.get();
                    server.subscribe(this);
                    sendMessage(nickname + ", добро пожаловать в чат!");
                    isAuthenticated = true;
                }
            } else if (msg.startsWith("/register ")) {
                // /register login pass nickname
                Optional<String> item = getRegisterNickname(msg);
                if (item.isPresent()) {
                    this.nickname = item.get();
                    server.subscribe(this);
                    sendMessage("Вы успешно зарегистрировались! " + nickname + ", добро пожаловать в чат!");
                    isAuthenticated = true;
                }
            } else if (msg.equals("/exit")) {
                break;
            } else {
                sendMessage("Вам необходимо авторизоваться");
            }
        }
        return isAuthenticated;
    }

    private Optional<String> getRegisterNickname(String msg) {
        String nickname = null;
        String[] tokens = msg.split(" ");
        if (tokens.length != 4) {
            sendMessage("Некорректный формат запроса");
        } else {
            String login = tokens[1];
            String password = tokens[2];
            nickname = tokens[3];
            if (server.getAuthenticationService().isLoginAlreadyExist(login)) {
                sendMessage("Указанный логин уже занят");
                nickname = null;
            }
            if (server.getAuthenticationService().isNicknameAlreadyExist(nickname)) {
                sendMessage("Указанный никнейм уже занят");
                nickname = null;
            }
            if (!server.getAuthenticationService().register(login, password, nickname)) {
                sendMessage("Не удалось пройти регистрацию");
                nickname = null;
            }
        }
        return Optional.ofNullable(nickname);
    }

    private Optional<String> getAuthNickname(String msg) {
        String nickname = null;
        String[] tokens = msg.split(" ");
        if (tokens.length != 3) {
            sendMessage("Некорректный формат запроса");
        } else {
            String login = tokens[1];
            String password = tokens[2];
            nickname = server.getAuthenticationService().getNicknameByLoginAndPassword(login, password);
            if (nickname == null) {
                sendMessage("Неправильный логин/пароль");
            }
            if (server.isNicknameActive(nickname)) {
                sendMessage("Указанная учетная запись уже занята. Попробуйте зайти позднее");
                nickname = null;
            }
        }
        return Optional.ofNullable(nickname);
    }

}