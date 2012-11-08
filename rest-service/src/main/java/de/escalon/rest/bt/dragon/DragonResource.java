package de.escalon.rest.bt.dragon;

import org.springframework.hateoas.Resource;

public class DragonResource extends Resource<Dragon> {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
