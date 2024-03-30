package ru.nicshal.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {

    public final static String IP_ADDRESS = "127.0.0.1";
    public final static int PORT_NUMBER = 9090;

    public static void main(String[] args) {
        try (Socket socket = new Socket(IP_ADDRESS, PORT_NUMBER);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Подключились к серверу");
            Thread thread = createThread(in);
            thread.start();
            startCommunication(out, thread);
            thread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void startCommunication(DataOutputStream out, Thread thread) throws IOException, InterruptedException {
        boolean isEnable = true;
        Scanner scanner = new Scanner(System.in);
        while (isEnable) {
            String request = scanner.nextLine();
            out.writeUTF(request);
            if (request.equals("/exit")) {
                isEnable = false;
            }
        }
    }

    private static Thread createThread(DataInputStream in) {
        return new Thread(() -> {
            try {
                boolean isEnableThread = true;
                while (isEnableThread) {
                    String msg = in.readUTF();
                    System.out.println(msg);
                    if (msg.endsWith("stop")) {
                        isEnableThread = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}