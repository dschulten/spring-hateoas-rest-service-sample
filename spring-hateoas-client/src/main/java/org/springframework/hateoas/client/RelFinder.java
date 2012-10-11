package org.springframework.hateoas.client;

import java.util.Map;

import org.springframework.hateoas.Link;

public interface RelFinder {

	Map<String, Link> findRels();

}
