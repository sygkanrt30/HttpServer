package ru.practise.http_server.processors;

import com.google.gson.Gson;
import ru.practise.http_server.HttpRequest;
import ru.practise.http_server.application.Product;
import ru.practise.http_server.application.ProductsService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PutProductProcessor implements RequestProcessor {
    private final ProductsService productsService;

    public PutProductProcessor(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public void execute(HttpRequest request, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Product newProduct = gson.fromJson(request.getBody(), Product.class);
        productsService.putProduct(newProduct);
        String response = """
            HTTP/1.1 200 OK\r
            Content-Type: text/html\r
            \r
            <html><body><h1>Removal is successfully completed</h1></body></html>""";
        output.write(response.getBytes(StandardCharsets.UTF_8));
    }
}