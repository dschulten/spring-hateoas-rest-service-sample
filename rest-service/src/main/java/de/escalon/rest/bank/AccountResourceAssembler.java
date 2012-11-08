package de.escalon.rest.bank;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class AccountResourceAssembler extends ResourceAssemblerSupport<Account, AccountResource> {

	public AccountResourceAssembler() {
		super(AccountController.class, AccountResource.class);
	}

	public AccountResource toResource(Account entity) {
		AccountResource resource = createResource(entity);
		return resource;
	}

}
