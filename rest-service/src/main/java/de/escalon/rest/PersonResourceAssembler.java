package de.escalon.rest;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PersonResourceAssembler extends ResourceAssemblerSupport<Person, PersonResource> {

	public PersonResourceAssembler() {
		super(PersonController.class, PersonResource.class);
	}

	public PersonResource toResource(Person person) {
		PersonResource resource = createResource(person);
		resource.setFirstname(person.getFirstname());
		resource.setLastname(person.getLastname());
		return resource;
	}

}
