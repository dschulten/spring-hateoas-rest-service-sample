package org.springframework.hateoas.client;

import org.springframework.hateoas.Link;


public interface RelFinder {

    Link findRel(String rel);

}
