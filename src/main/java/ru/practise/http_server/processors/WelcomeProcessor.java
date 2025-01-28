package ru.practise.http_server.processors;

import ru.practise.http_server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class WelcomeProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        String response = """
                HTTP/1.1 200 OK\r
                Content-Type: text/html\r
                \r
                <html><body><h1>Welcome Page</h1></body></html>""";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }

}