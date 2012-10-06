package de.escalon.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductAccess productAccess;

    @RequestMapping(value = "/persons/{personId}", method = RequestMethod.GET)
    public HttpEntity<List<ProductResource>> getProductsOfPerson(Long personId) {
        Iterable<? extends Product> products = productAccess.getProductsOfPerson(personId);
        ProductResourceAssembler assembler = new ProductResourceAssembler();
        List<ProductResource> resources = assembler.toResources(products);
        return new HttpEntity<List<ProductResource>>(resources);
    }

}
