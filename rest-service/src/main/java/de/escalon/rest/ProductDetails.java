package de.escalon.rest;

import java.util.Map;

import org.springframework.hateoas.Identifiable;

public class ProductDetails implements Identifiable<Long> {

	private Long id;

	private Map<String, String> details;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

}
