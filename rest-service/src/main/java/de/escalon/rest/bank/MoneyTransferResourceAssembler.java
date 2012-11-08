package de.escalon.rest.bank;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public class MoneyTransferResourceAssembler extends ResourceAssemblerSupport<MoneyTransfer, MoneyTransferResource>{

	public MoneyTransferResourceAssembler() {
		super(MoneyTransferController.class, MoneyTransferResource.class);
	}

	public MoneyTransferResource toResource(MoneyTransfer entity) {
		MoneyTransferResource resource = createResource(entity);
		resource.setFrom(entity.getFrom());
		resource.setTo(entity.getTo());
		resource.setAmount(entity.getAmount());
		return resource;
	}
	
}
