package de.escalon.rest;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class ProductResourceAssembler extends ResourceAssemblerSupport<Product, ProductResource> {

	public ProductResourceAssembler() {
		super(ProductController.class, ProductResource.class);
	}

	public ProductResource toResource(Product entity) {
		ProductResource to = createResource(entity);
		to.setProductName(entity.getProductName());
		return to;
	}

}
