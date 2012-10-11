package de.escalon.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductAccess productAccess;

	@RequestMapping(value = "/persons/{personId}", method = RequestMethod.GET)
	public HttpEntity<List<ProductResource>> getProductsOfPerson(@PathVariable Long personId) {
		Iterable<? extends Product> products = productAccess.getProductsOfPerson(personId);
		ProductResourceAssembler assembler = new ProductResourceAssembler();
		List<ProductResource> resources = assembler.toResources(products);
		return new HttpEntity<List<ProductResource>>(resources);
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public HttpEntity<ProductResource> show(@PathVariable Long productId) {
		Product product = productAccess.getProduct(productId);
		ProductResourceAssembler assembler = new ProductResourceAssembler();
		ProductResource resource = assembler.toResource(product);
		return new HttpEntity<ProductResource>(resource);
	}

}
