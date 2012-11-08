package org.springframework.hateoas.client;


public class FollowRelAction implements Action {

	private String rel;

	public FollowRelAction(String rel) {
		super();
		this.rel = rel;
	}

	public Browsable execute(Browsable browsable, Browser browser) {
		browser.followRel(rel);
		return browser.getCurrentResource();
	}

}
