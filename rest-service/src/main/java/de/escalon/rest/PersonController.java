package de.escalon.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.action.ActionDescriptor;
import org.springframework.hateoas.action.Hidden;
import org.springframework.hateoas.mvc.ControllerFormBuilder;
import org.springframework.hateoas.sample.SamplePersonController;
import org.springframework.http.HttpEntity;
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

	@Autowired
	ProductAccess productAccess;

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
		Resources<PersonResource> wrapped = new Resources<PersonResource>(resources, linkTo(PersonController.class)
				.withSelfRel());
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
		PageMetadata pageMetadata = new PageMetadata(size, number, totalElements, totalPages);
		PagedResources<PersonResource> pagedResources = new PagedResources<PersonResource>(resources, pageMetadata);
		return new HttpEntity<PagedResources<PersonResource>>(pagedResources);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity<PersonResource> show(@PathVariable Long id) {
		Person person = personAccess.getPerson(id);
		PersonResourceAssembler assembler = new PersonResourceAssembler();
		PersonResource resource = assembler.toResource(person);
		Link describedBy = new Link("http://example.com/doc#customer", "describedBy");
		resource.add(describedBy);
		resource.add(linkTo(methodOn(ProductController.class).getProductsOfPerson(id)).withRel("products"));
		return new HttpEntity<PersonResource>(resource);
	}

	@RequestMapping(value = "/customer", method = RequestMethod.GET)
	public HttpEntity<ActionDescriptor> searchPersonForm() {
		ActionDescriptor form = ControllerFormBuilder.createFormFor(methodOn(PersonController.class).showPerson(1L),
				"searchPerson");
		return new HttpEntity<ActionDescriptor>(form);
	}

	@RequestMapping(value = "/customer", method = RequestMethod.GET, params = { "personId" })
	public HttpEntity<? extends Object> showPerson(@RequestParam(value = "personId") Long personId) {

		Person person = personAccess.getPerson(personId);
		PersonResourceAssembler assembler = new PersonResourceAssembler();
		PersonResource resource = assembler.toResource(person);
		Link describedBy = new Link("http://example.com/doc#customer", "describedBy");
		resource.add(describedBy);

		// variant 1: products are separate
		resource.add(linkTo(methodOn(ProductController.class).getProductsOfPerson(personId)).withRel("products"));

		// variant 2: products are embedded in customer
		// Iterable<? extends Product> products = productAccess.getProductsOfPerson(personId);
		// ProductResourceAssembler prodAssembler = new ProductResourceAssembler();
		// List<ProductResource> prodResources = prodAssembler.toResources(products);
		// for (ProductResource productResource : prodResources) {
		// productResource.add(new Link("http://example.com/doc#product", "describedBy"));
		// }
		// Resources<ProductResource> wrapped = new Resources<ProductResource>(prodResources, new Link(
		// "http://example.com/doc#products", "describedBy"));
		// resource.setProducts(wrapped);

		return new HttpEntity<PersonResource>(resource);
	}

	@RequestMapping(value = "/customer/{personId}/edit")
	public HttpEntity<ActionDescriptor> editPersonForm(@RequestParam Long personId) {

		Person person = personAccess.getPerson(personId);

		ActionDescriptor descriptor = ControllerFormBuilder.createFormFor(methodOn(SamplePersonController.class)
				.editPerson(person.getId(), person.getFirstname(), person.getLastname()), "changePerson");

		return new HttpEntity<ActionDescriptor>(descriptor);
	}

	@RequestMapping(value = "/customer", method = RequestMethod.PUT, params = { "personId", "firstname", "lastname" })
	public HttpEntity<PersonResource> editPerson(@RequestParam @Hidden Long personId, @RequestParam String firstname,
			@RequestParam String lastname) {

		Person person = personAccess.getPerson(personId);

		person.setFirstname(firstname);
		person.setLastname(lastname);
		PersonResourceAssembler assembler = new PersonResourceAssembler();
		PersonResource resource = assembler.toResource(person);

		return new HttpEntity<PersonResource>(resource);
	}
}