package ru.nicshal.http.server;

import ru.nicshal.http.server.handlers.HttpRequestHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nicshal.http.server.repositories.PostgresProductRepositoryImpl;
import ru.nicshal.http.server.repositories.ProductRepository;
import ru.nicshal.http.server.repositories.StorageProductRepositoryImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final Logger logger = LogManager.getLogger(HttpServer.class.getName());
    private final int port;
    private final Dispatcher dispatcher;
    private final ExecutorService executorService;
    private final ProductRepository productRepository;

    public HttpServer(int port, int threadCount, String repositoryType) {
        this.port = port;
        this.dispatcher = new Dispatcher();
        this.executorService = Executors.newFixedThreadPool(threadCount);
        if ("Postgres".equals(repositoryType)) {
            productRepository = new PostgresProductRepositoryImpl();
        } else {
            productRepository = new StorageProductRepositoryImpl();
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            productRepository.init();
            logger.info("Сервер запущен на порту: " + port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executeHttpRequestHandler(socket);
                } catch (Exception e) {
                    logger.error("Возникла ошибка при обработке нового подключения");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    private void executeHttpRequestHandler(Socket socket) throws IOException {
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(this, socket);
        executorService.execute(httpRequestHandler);
    }

}