package ru.nicshal.chat.server;

public class ServerApplication {

    public final static int PORT_NUMBER = 9090;

    public static void main(String[] args) {
        new Server(PORT_NUMBER).start();
    }

}