package ru.practise.http_server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class HttpServer {
    private final int port;
    private final Dispatcher dispatcher;
    private static final Logger LOGGER = LogManager.getLogger(HttpServer.class);

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.info("Сервер запущен на порту: {}", port);
            var serv = Executors.newCachedThreadPool();
            while (true) {
                Socket socket = serverSocket.accept();
                serv.execute(() -> {
                    try {
                        applicationLifecycle(socket);
                    } catch (IOException e) {
                        LOGGER.debug(e.getMessage());
                    } finally {
                        if (socket != null && !socket.isClosed()) {
                            try {
                                socket.close();
                                LOGGER.info("Сокет закрыт: {}\n", socket);
                            } catch (IOException e) {
                                LOGGER.error("Ошибка при закрытии сокета: {}", e.getMessage());
                            }
                        }
                    }
                });
            }
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    private void applicationLifecycle(Socket socket) throws IOException {
        byte[] buffer = new byte[8192];
        int n = socket.getInputStream().read(buffer);
        if (n == -1) {
            return;
        }
        LOGGER.info("Подключился новый клиент");
        HttpRequest request = new HttpRequest(new String(buffer, 0, n));
        request.info(false);
        dispatcher.execute(request, socket.getOutputStream());
    }
}
