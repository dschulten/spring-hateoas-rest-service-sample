package org.springframework.hateoas.client;

public class FollowRelAction implements Action {

	private String rel;

	public FollowRelAction(String rel) {
		super();
		this.rel = rel;
	}

	public Browsable execute(Browser browser) {
		browser.followRel(rel);
		return browser.getCurrentResource();
	}

	public boolean possibleOn(Browsable currentResource) {
		return currentResource.getRel(rel) != null;
	}

}
