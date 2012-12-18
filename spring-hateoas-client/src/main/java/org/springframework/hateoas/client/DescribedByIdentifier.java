package org.springframework.hateoas.client;

import java.util.List;
import java.util.Map;

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
		Map<String, List<Link>> rels = browsable.getRels();

		List<Link> describedByRels = rels.get("describedBy");
		// find
		boolean ret = false;
		if (describedByRels != null) {
			for (Link link : describedByRels) {
				if (this.descriptor.equals(link)) {
					ret = true;
					break;
				}
			}
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
		String href = descriptor.getHref();
		return "DescribedByIdentifier [name=" + href.substring(href.lastIndexOf("/") + 1) + " href=" + href + "]";
	}

}