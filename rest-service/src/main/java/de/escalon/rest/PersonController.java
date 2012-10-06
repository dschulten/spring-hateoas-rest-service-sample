package de.escalon.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/people")
public class PersonController {

    @Autowired
    PersonAccess personAccess;

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<List<PersonResource>> showAll() {
        Iterable<? extends Person> people = personAccess.getPersons();
        PersonResourceAssembler assembler = new PersonResourceAssembler();
        List<PersonResource> resources = assembler.toResources(people);
        return new HttpEntity<List<PersonResource>>(resources);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<PersonResource> show(@PathVariable Long id) {
        Person person = personAccess.getPerson(id);
        PersonResourceAssembler assembler = new PersonResourceAssembler();
        PersonResource resource = assembler.toResource(person);
        return new HttpEntity<PersonResource>(resource);
    }
}