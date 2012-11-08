package org.springframework.hateoas.client;

public interface Action {

	Browsable execute(Browsable browsable, Browser browser);

}
