package de.escalon.rest.bt.dragon;

import org.springframework.hateoas.Identifiable;

public class Dragon implements Identifiable<Long>{

	private String name;
	private String location;
	private String homeLocation;

	public Dragon(String name, String homeLocation) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return 1L;
	}

	public String getLocation() {
		return location;
	}

	public String getHomeLocation() {
		return homeLocation;
	}



}
