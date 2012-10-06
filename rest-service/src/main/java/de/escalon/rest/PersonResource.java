package de.escalon.rest;

import org.springframework.hateoas.ResourceSupport;

public class PersonResource extends ResourceSupport {

    String firstname;
    String lastname;

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