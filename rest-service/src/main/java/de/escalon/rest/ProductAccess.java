package de.escalon.rest;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class ProductAccess {

    public Iterable<? extends Product> getProductsOfPerson(Long personId) {
        Product product = new Product(1L, "Washing Machine");
        return Arrays.asList(product);

    }

}
