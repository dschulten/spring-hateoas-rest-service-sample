package org.springframework.hateoas.client;


public interface Identifier {
	boolean foundIn(Browsable browsable);

	Identifier within(Identifier identifier);

	Identifier getContainingIdentifier();

	void setContainedIdentifier(Identifier identifier);

	Identifier getContainedIdentifier();

}