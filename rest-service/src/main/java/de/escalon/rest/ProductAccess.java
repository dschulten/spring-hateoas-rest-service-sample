package de.escalon.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ProductAccess {

    private List<Product> products = Arrays.asList(new Product(1L,
            "Starfleet Standard Tricorder"), new Product(2L, "Flux Capacitor"),
            new Product(3L, "Phase Calibrator"));

    public Iterable<? extends Product> getProductsOfPerson(Long personId) {

        if (personId == 1L)
            return products.subList(0, 1);
        else if (personId == 2L)
            return products.subList(1, 2);
        else
            return products;

    }

    public Product getProduct(Long productId) {
        return products.get((int)productId.longValue() - 1);
    }

}
