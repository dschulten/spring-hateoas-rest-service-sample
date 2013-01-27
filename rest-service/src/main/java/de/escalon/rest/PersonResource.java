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

	public String getGivenName() {
		return firstname;
	}

	public void setGivenName(String firstname) {
		this.firstname = firstname;
	}

	public String getFamilyName() {
		return lastname;
	}

	public void setFamilyName(String lastname) {
		this.lastname = lastname;
	}
}