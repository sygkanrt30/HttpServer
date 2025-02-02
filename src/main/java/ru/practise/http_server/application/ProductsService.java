package ru.practise.http_server.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.practise.http_server.BadRequestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductsService {
    private final List<Product> products;
    public static final Logger LOGGER = LogManager.getLogger(ProductsService.class);

    public ProductsService() {
        this.products = new ArrayList<>(Arrays.asList(
                new Product(1L, "Milk"),
                new Product(2L, "Bread"),
                new Product(3L, "Cheese")
        ));
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    public Product getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().get();
    }

    public void createNewProduct(Product product) {
        Long newId = products.stream().mapToLong(Product::getId).max().getAsLong() + 1;
        products.add(new Product(newId, product.getTitle()));
        LOGGER.info("Добавлен продукт: {}",product);
    }

    public void deleteAllProducts(){
        products.clear();
        LOGGER.info("Удалены все продукты");
    }

    public void deleteProduct(Long id){
        for (Product product : products) {
            if (id.equals(product.getId())){
                products.remove(product);
                LOGGER.info("Удален продукт с id: {}", id);
                return;
            }
        }
        throw new BadRequestException(
                "VALIDATION_ERROR_MISSING_FIELD",
                "Такого 'id' не существует"
        );

    }

    public void putProduct(Product product){
        for (Product value : products) {
            if (value.getId().equals(product.getId())) {
                value.setTitle(product.getTitle());
                return;
            }
        }
        createNewProduct(product);
    }
}
