package de.escalon.rest;

import org.springframework.hateoas.Resource;

public class ProductResource extends Resource<Product> {

	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
