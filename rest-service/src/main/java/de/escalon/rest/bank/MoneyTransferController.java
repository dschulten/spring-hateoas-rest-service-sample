package de.escalon.rest.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transfer")
public class MoneyTransferController {

	@Autowired
	MoneyTransferAccess moneyTransferAccess;
	@RequestMapping("/{id}")
	public HttpEntity<MoneyTransferResource> getTransfer(@PathVariable Long id) {
		MoneyTransfer moneyTransfer = moneyTransferAccess.getTransfer(id);
		
		MoneyTransferResourceAssembler assembler = new MoneyTransferResourceAssembler();
		MoneyTransferResource resource = assembler.toResource(moneyTransfer);
		return new HttpEntity<MoneyTransferResource>(resource);
	}

}
