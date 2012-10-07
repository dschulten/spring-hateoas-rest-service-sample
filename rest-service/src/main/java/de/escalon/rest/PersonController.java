package de.escalon.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonAccess personAccess;

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<PersonResource>> showAll() {
        Iterable<? extends Person> people = personAccess.getPersons();
        PersonResourceAssembler assembler = new PersonResourceAssembler();
        List<PersonResource> resources = assembler.toResources(people);
        return new HttpEntity<List<PersonResource>>(resources);
    }

    @RequestMapping(value = "/resources", method = RequestMethod.GET)
    public HttpEntity<Resources<PersonResource>> showAllAsResources() {
        Iterable<? extends Person> people = personAccess.getPersons();
        PersonResourceAssembler assembler = new PersonResourceAssembler();
        List<PersonResource> resources = assembler.toResources(people);
        // Resources allows to add links once for the entire list
        // provides the list as content attribute
        Resources<PersonResource> wrapped = new Resources<PersonResource>(
                resources, linkTo(PersonController.class).withSelfRel());
        return new HttpEntity<Resources<PersonResource>>(wrapped);
    }

    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public HttpEntity<PagedResources<PersonResource>> showAllPaged() {
        Iterable<? extends Person> people = personAccess.getPersons();
        PersonResourceAssembler assembler = new PersonResourceAssembler();
        List<PersonResource> resources = assembler.toResources(people);
        long size = 5;
        long number = 1;
        long totalElements = resources.size();
        long totalPages = totalElements / size;
        PageMetadata pageMetadata = new PageMetadata(size, number,
                totalElements, totalPages);
        PagedResources<PersonResource> pagedResources = new PagedResources<PersonResource>(
                resources, pageMetadata);
        return new HttpEntity<PagedResources<PersonResource>>(pagedResources);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<PersonResource> show(@PathVariable Long id) {
        Person person = personAccess.getPerson(id);
        PersonResourceAssembler assembler = new PersonResourceAssembler();
        PersonResource resource = assembler.toResource(person);
        resource.add(linkTo(
                methodOn(ProductController.class).getProductsOfPerson(id))
                .withRel("products"));
        return new HttpEntity<PersonResource>(resource);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public HttpEntity<? extends Object> search(
            @RequestParam(value = "personId", required = false) Long personId) {
        if (personId == null) {
            String searchPerson = "searchPerson";
            String backRel = "back";
            String form = "<?xml version='1.0' encoding='ISO-8859-1' ?>" + // formatter
//                    "<!DOCTYPE html>" + //
                    "<html xmlns='http://www.w3.org/1999/xhtml'>" + //
                    "  <head>" + //
                    "    <title>Simple Form</title>" + //
                    "    <link rel='help' title='Hilfe' href='../../editorial/index.htm' />" +//
                    "  </head>" + //
                    "  <body>" + //
                    "    <form action='/people/search' name='" + searchPerson + "'>" + //
                    "      <h1>Enter Person Id:</h1>" + //
                    "      <input type='number' name='personId' />" + //
                    "      <input type='submit' value='Find' />" + //
                    "    </form>" + //
                    "    <a href='/' rel='" + backRel  + "'>Back</a>" + //
                    "  </body>" + //
                    "</html>";


            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "text/html");
            HttpEntity<String> httpEntity = new HttpEntity<String>(form, headers);
            return httpEntity;
        } else {
            Person person = personAccess.getPerson(personId);
            PersonResourceAssembler assembler = new PersonResourceAssembler();
            PersonResource resource = assembler.toResource(person);
            resource.add(linkTo(
                    methodOn(ProductController.class).getProductsOfPerson(personId))
                    .withRel("products"));
            return new HttpEntity<PersonResource>(resource);
        }
    }
}