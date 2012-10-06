package de.escalon.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PersonAccess {

    private Iterable<Person> people = PersonBuilder.aPerson().buildMany(100);

    private Map<Long, Person> peopleIndex = indexPeople(people);

    public Iterable<? extends Person> getPersons() {
        return people;
    }

    public Person getPerson(Long id) {
        return peopleIndex.get(id);

    }

    private Map<Long, Person> indexPeople(Iterable<Person> people) {
        Map<Long, Person> personIndex = new HashMap<Long, Person>();
        for (Person person : people) {
            personIndex.put(person.getId(), person);
        }
        return personIndex;
    }

}
