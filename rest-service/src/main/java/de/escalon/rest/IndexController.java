package de.escalon.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json, application/xml", produces = { "application/json" })
    public HttpEntity<List<Link>> showLinks() {
        List<Link> links = new ArrayList<Link>();
        Link people = linkTo(PersonController.class).withRel("people");
        Link person = linkTo(methodOn(PersonController.class).show(1L)).withRel("person");
        Link products = linkTo(ProductController.class).withRel("products");
        Link personProducts = linkTo(PersonProductController.class, 1L)
                .withRel("personProducts");

        links.add(people);
        links.add(person);
        links.add(products);
        links.add(personProducts);
        return new HttpEntity<List<Link>>(links);
    }

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=text/html, text/xhtml", produces = {
            "text/html", "text/xhtml" })
    public String showAllHtml(Map<String, Object> model) {
        List<Link> links = new ArrayList<Link>();
        links.add(linkTo(IndexController.class).withSelfRel());
        links.add(linkTo(PersonController.class).withRel("people"));
        model.put("links", links); // referenced by ${links} in the jsp
        return "home";
    }
}
