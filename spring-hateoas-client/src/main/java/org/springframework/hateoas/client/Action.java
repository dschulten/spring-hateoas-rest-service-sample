package org.springframework.hateoas.client;

public interface Action {

	Browsable execute(Browser browser);

	boolean possibleOn(Browsable currentResource);

}
