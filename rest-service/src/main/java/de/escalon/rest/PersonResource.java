package de.escalon.rest;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

public class PersonResource extends Resource<Person> {

	String firstname;
	String lastname;
	
	Resources<ProductResource> products;

	public Resources<ProductResource> getProducts() {
		return products;
	}

	public void setProducts(Resources<ProductResource> wrapped) {
		this.products = wrapped;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}