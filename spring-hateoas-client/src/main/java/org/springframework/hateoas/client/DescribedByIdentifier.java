package org.springframework.hateoas.client;

import org.springframework.hateoas.Link;


public class DescribedByIdentifier implements Identifier {
	Link descriptor;

	Identifier containingIdentifier;

	private Identifier containedIdentifier;

	public Identifier getContainedIdentifier() {
		return containedIdentifier;
	}

	public DescribedByIdentifier(String descriptor) {
		super();
		this.descriptor = new Link(descriptor, "describedBy");
	}

	public boolean foundIn(Browsable browsable) {
		final boolean ret;
		if (this.descriptor.equals(browsable.getRel("describedBy"))) {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	public Identifier within(Identifier identifier) {
		this.containingIdentifier = identifier;
		identifier.setContainedIdentifier(this);
		return identifier;
	}



	public void setContainedIdentifier(Identifier identifier) {
		this.containedIdentifier = identifier;

	}

	public Identifier getContainingIdentifier() {
		return containingIdentifier;
	}

	@Override
	public String toString() {
		return "DescribedByIdentifier [descriptor=" + descriptor + "]";
	}


}