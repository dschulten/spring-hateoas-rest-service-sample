package de.escalon.rest;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/people/{personId}/products")
public class PersonProductController {

    private ProductAccess productAccess;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HttpEntity<List<ProductResource>> getProductsOfPerson(Long personId) {
        Iterable<? extends Product> people = productAccess.getProductsOfPerson(personId);
        ProductResourceAssembler assembler = new ProductResourceAssembler();
        List<ProductResource> resources = assembler.toResources(people);
        return new HttpEntity<List<ProductResource>>(resources);
    }

}
