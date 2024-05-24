package ru.nicshal.http.server;

import static ru.nicshal.http.server.utils.ApplicationConst.*;

public class ServerApplication {

    public static void main(String[] args) {
        new HttpServer(PORT_NUMBER, THREAD_COUNT, REPOSITORY_TYPE).start();
    }

}