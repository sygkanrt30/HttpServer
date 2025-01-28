package ru.practise.http_server;



import ru.practise.http_server.processors.CalculatorProcessor;
import ru.practise.http_server.processors.Default404Processor;
import ru.practise.http_server.processors.RequestProcessor;
import ru.practise.http_server.processors.WelcomeProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, RequestProcessor> router;
    private final Default404Processor default404Processor;

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("/calc", new CalculatorProcessor());
        this.router.put("/welcome", new WelcomeProcessor());
        this.default404Processor = new Default404Processor();
    }

    public void execute(HttpRequest request, OutputStream output) throws IOException {
        if (!router.containsKey(request.getUri())) {
            default404Processor.execute(request, output);
            return;
        }
        router.get(request.getUri()).execute(request, output);
    }
}
