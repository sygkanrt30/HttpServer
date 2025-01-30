package ru.practise.http_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class HttpServer {
    private final int port;
    private final Dispatcher dispatcher;

    public HttpServer(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            var serv = Executors.newCachedThreadPool();
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    serv.execute(() -> {
                        try {
                            applicationLifecycle(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applicationLifecycle(Socket socket) throws IOException {
        byte[] buffer = new byte[8192];
        int n = socket.getInputStream().read(buffer);
        if (n == -1) {
            return;
        }
        System.out.println("Подключился новый клиент");
        HttpRequest request = new HttpRequest(new String(buffer, 0, n));
        request.info(true);
        dispatcher.execute(request, socket.getOutputStream());
    }
}
