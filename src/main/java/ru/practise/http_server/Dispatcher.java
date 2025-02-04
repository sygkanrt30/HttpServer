package ru.practise.http_server;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.practise.http_server.application.ProductsService;
import ru.practise.http_server.processors.*;
import ru.practise.http_server.processors.default_processor.Default400Processor;
import ru.practise.http_server.processors.default_processor.Default404Processor;
import ru.practise.http_server.processors.default_processor.Default500Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private static final Logger LOGGER = LogManager.getLogger(Dispatcher.class);
    private final Map<String, RequestProcessor> router;
    private final Default400Processor default400Processor;
    private final Default404Processor default404Processor;
    private final Default500Processor default500Processor;

    public Dispatcher() {
        ProductsService productsService = new ProductsService();
        this.router = new HashMap<>();
        this.router.put("GET /calc", new CalculatorProcessor());
        this.router.put("GET /welcome", new WelcomeProcessor());
        this.router.put("GET /products", new GetProductsProcessor(productsService));
        this.router.put("POST /products", new CreateProductProcessor(productsService));
        this.router.put("DELETE /products", new DeleteProductProcessor(productsService));
        this.router.put("PUT /products", new PutProductProcessor(productsService));
        this.default400Processor = new Default400Processor();
        this.default404Processor = new Default404Processor();
        this.default500Processor = new Default500Processor();
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        try {
            if (!router.containsKey(request.getRoutingKey())) {
                default404Processor.execute(request, output);
                return;
            }
            router.get(request.getRoutingKey()).execute(request, output);
        } catch (BadRequestException e) {
            LOGGER.error(e.getDescription());
            request.setErrorCause(e);
            default400Processor.execute(request, output);
        } catch (Exception e) {
            LOGGER.error(e.getCause().toString());
            default500Processor.execute(request, output);
        }
    }
}
