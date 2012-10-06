package de.escalon.rest;

import org.springframework.hateoas.ResourceSupport;

public class ProductResource extends ResourceSupport {

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
