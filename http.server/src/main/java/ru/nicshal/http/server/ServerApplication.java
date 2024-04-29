package ru.nicshal.http.server;

public class ServerApplication {

    public final static int PORT_NUMBER = 8189;

    public static void main(String[] args) {
        new HttpServer(PORT_NUMBER).start();
    }

}