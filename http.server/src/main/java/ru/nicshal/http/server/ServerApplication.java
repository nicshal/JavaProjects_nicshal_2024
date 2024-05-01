package ru.nicshal.http.server;

public class ServerApplication {

    public static final int PORT_NUMBER = 8189;
    private static final int THREAD_COUNT = 10;

    public static void main(String[] args) {
        new HttpServer(PORT_NUMBER, THREAD_COUNT).start();
    }

}