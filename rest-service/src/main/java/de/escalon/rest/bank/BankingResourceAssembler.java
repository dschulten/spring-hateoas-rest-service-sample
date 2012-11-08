package de.escalon.rest.bank;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class BankingResourceAssembler extends ResourceAssemblerSupport<Banking, BankingResource> {

	public BankingResourceAssembler() {
		super(BankingController.class, BankingResource.class);
	}

	public BankingResource toResource(Banking entity) {
		BankingResource bankingResource = createResource(entity, "hello");
		bankingResource.setBankName(entity.getBankName());
		bankingResource.setBlz(entity.getBlz());
		bankingResource.setBic(entity.getBic());
		return bankingResource;
	}

}
