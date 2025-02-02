package ru.practise.http_server.processors;

import com.google.gson.Gson;
import ru.practise.http_server.HttpRequest;
import ru.practise.http_server.application.ProductsService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteProductProcessor implements RequestProcessor {
    private final ProductsService productsService;

    public DeleteProductProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        if (request.containsParameter("id")) {
            Long id = Long.parseLong(request.getParameter("id"));
            productsService.deleteProduct(id);
        } else {
            productsService.deleteAllProducts();
        }
        String response = """
                HTTP/1.1 200 OK\r
                Content-Type: text/html\r
                \r
                <html><body><h1>Removal is successfully completed</h1></body></html>""";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
