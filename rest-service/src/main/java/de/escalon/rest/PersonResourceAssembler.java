package de.escalon.rest;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class PersonResourceAssembler extends ResourceAssemblerSupport<Person, PersonResource> {

	public PersonResourceAssembler() {
		super(PersonController.class, PersonResource.class);
	}

	public PersonResource toResource(Person person) {
		PersonResource resource = createResource(person);
		resource.setGivenName(person.getFirstname());
		resource.setFamilyName(person.getLastname());
		return resource;
	}

}
