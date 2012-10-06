package de.escalon.rest;

import org.springframework.hateoas.Identifiable;

public class Product implements Identifiable<Long> {
    private String productName;

    private Long id;

    public Product(Long id, String productName) {
        super();
        this.id = id;
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

}
