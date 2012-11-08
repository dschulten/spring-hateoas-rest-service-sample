package de.escalon.rest.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private BankingAccess bankingAccess;

	@RequestMapping(value = { "/{accountNumber}" }, method = RequestMethod.GET, params = { "accountNumber" })
	HttpEntity<AccountResource> bankAccount(@PathVariable String accountNumber) {
		Banking banking = bankingAccess.getBanking();
		AccountResourceAssembler assembler = new AccountResourceAssembler();
		AccountResource resource = assembler.toResource(banking.getAccount(accountNumber));
		resource.add(new Link("http://localhost:8080/banking/ns/account", "describedBy"));
		return new HttpEntity<AccountResource>(resource);
	}
	

}
