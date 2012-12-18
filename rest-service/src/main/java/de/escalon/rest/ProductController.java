package de.escalon.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkToMethod;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.on;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
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
	public HttpEntity<Resources<ProductResource>> getProductsOfPerson(@PathVariable Long personId) {
		Iterable<? extends Product> products = productAccess.getProductsOfPerson(personId);

		ProductResourceAssembler assembler = new ProductResourceAssembler();
		List<ProductResource> resources = assembler.toResources(products);
		for (ProductResource productResource : resources) {
			//productResource.add(new Link("http://example.com/doc#product", "describedBy"));
		}
		// Resources allows to add links once for the entire list
		// provides the list as content attribute
		Resources<ProductResource> wrapped = new Resources<ProductResource>(resources, linkToMethod(
				on(ProductController.class).getProductsOfPerson(personId)).withSelfRel());
		Link describedBy = new Link("http://example.com/doc#products", "describedBy");
		wrapped.add(describedBy);
		return new HttpEntity<Resources<ProductResource>>(wrapped);

	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public HttpEntity<ProductResource> show(@PathVariable Long productId) {
		Product product = productAccess.getProduct(productId);
		ProductResourceAssembler assembler = new ProductResourceAssembler();
		ProductResource resource = assembler.toResource(product);
		Link describedBy = new Link("http://example.com/doc#product", "describedBy");
		resource.add(describedBy);
		return new HttpEntity<ProductResource>(resource);
	}

}
