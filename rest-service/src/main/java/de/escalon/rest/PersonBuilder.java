package de.escalon.rest;

import java.util.ArrayList;
import java.util.List;

public class PersonBuilder {

	private static long count = 0;

	String firstname = "Thorin";
	String lastname = "Oakenshield";
	Long id;

	public static PersonBuilder aPerson() {
		return new PersonBuilder();
	}

	public Person build() {
		synchronized (this) {
			count++;
		}
		Person person = new Person();
		person.setFirstname(firstname);
		person.setLastname(lastname + count);
		person.setId(count);
		return person;
	}

	public Iterable<Person> buildMany(int numberOf) {
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < numberOf; i++) {
			persons.add(build());
		}
		return persons;
	}
}
