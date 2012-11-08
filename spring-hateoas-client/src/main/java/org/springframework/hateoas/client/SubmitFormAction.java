package org.springframework.hateoas.client;

import org.springframework.hateoas.util.Args;

public class SubmitFormAction implements Action {

	private Args args;
	private String formName;

	public SubmitFormAction(String formName, Args args) {
				this.formName = formName;
				this.args = args;
	}

	public Browsable execute(Browsable browsable, Browser browser) {
		browser.submitForm(formName, args);
		return browser.getCurrentResource();
	}

}
