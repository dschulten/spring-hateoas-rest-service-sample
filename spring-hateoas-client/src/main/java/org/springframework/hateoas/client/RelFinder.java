package org.springframework.hateoas.client;


import org.springframework.hateoas.Link;
import org.springframework.util.MultiValueMap;

public interface RelFinder {

	MultiValueMap<String, Link> findRels();

}
