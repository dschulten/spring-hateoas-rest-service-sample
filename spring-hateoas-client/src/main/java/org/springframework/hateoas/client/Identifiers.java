package org.springframework.hateoas.client;

public class Identifiers {
	
	public static Identifier describedBy(String descriptor) {
		return new DescribedByIdentifier(descriptor);
	}
}
