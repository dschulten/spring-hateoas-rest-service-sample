package de.escalon.rest.bank;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.createForm;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkToMethod;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.on;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.FormDescriptor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

@Controller
@RequestMapping("/banking")
public class BankingController {

	@Autowired
	private BankingAccess bankingAccess;
	@Autowired
	private MoneyTransferAccess moneyTransferAccess;

	@RequestMapping
	public HttpEntity<BankingResource> getIndex() {

		// TODO can I build a BankingResource which embeds:
		// banking has account has transfer
		// so that the client follows the rels account, transfer
		// filling out the forms, like
		// get a transfer of account of banking

		BankingResourceAssembler assembler = new BankingResourceAssembler();
		Banking banking = bankingAccess.getBanking();
		BankingResource resource = assembler.toResource(banking);
		resource.add(new Link("http://localhost:8080/banking/ns/bank", "describedBy"));
		resource.add(ControllerLinkBuilder.linkToMethod(on(BankingController.class).bankAccountForm()).withRel(
				"http://localhost:8080/banking/ns/account"));
		return new HttpEntity<BankingResource>(resource);
	}

	@RequestMapping("/ns/bank")
	public HttpEntity<Model> getBankDescription() {
		// must contain a bank with property account with property transfer
		// is it possible to use json-ld or rdf-a to describe that embedded in bankingResource?
		// must read this after reading the index and build the rel path
		// to the target resource /transfer
		// use Jena for this?

		// // some definitions
		// static String personURI = "http://somewhere/JohnSmith";
		// static String fullName = "John Smith";
		//
		// // create an empty Model
		// Model model = ModelFactory.createDefaultModel();
		//
		// // create the resource
		// Resource johnSmith = model.createResource(personURI);
		//
		// // add the property
		// johnSmith.addProperty(VCARD.FN, fullName);

		// create an empty Model
		Model model = ModelFactory.createDefaultModel();

		// some definitions
		String bankURI = "http://localhost:8080/banking/ns/bank";
		String accountURI = "http://localhost:8080/banking/ns/account";
		String transferURI = "http://localhost:8080/banking/ns/transfer";

		// create the resource
		Resource bank = model.createResource(bankURI);
		Resource account = model.createResource(accountURI);
		Resource transfer = model.createResource(transferURI);

		// bank is a class, having a property 'account'
		model.add(bank, RDF.type, RDFS.Class);
		model.add(account, RDF.type, RDF.Property);
		model.add(account, RDFS.domain, bank);

		model.add(account, RDF.type, RDFS.Class);
		model.add(transfer, RDFS.domain, account);

		// TODO how to navigate/query the model
		// and how to describe the goal for the client
		// goal is the transfer, that much is clear
		// can I create a path of rels to the goal from the model?

		return new HttpEntity<Model>(model);
	}

	@RequestMapping(value = { "/account" }, method = RequestMethod.GET)
	public HttpEntity<FormDescriptor> bankAccountForm() {
		FormDescriptor form = ControllerLinkBuilder.createForm("bankAccountForm",
				on(BankingController.class).bankAccount(null));
		return new HttpEntity<FormDescriptor>(form);
	}

	@RequestMapping(value = { "/account" }, method = RequestMethod.GET, params = { "accountNumber" })
	public HttpEntity<AccountResource> bankAccount(
			@RequestParam(value = "accountNumber", required = true) String accountNumber) {
		Banking banking = bankingAccess.getBanking();
		AccountResourceAssembler assembler = new AccountResourceAssembler();
		Account account = banking.getAccount(accountNumber);
		AccountResource resource = assembler.toResource(account);

		resource.add(new Link("http://localhost:8080/banking/ns/account", "describedBy"));
		resource.add(linkToMethod(on(BankingController.class).moneyTransferForm(account.getNumber())).withRel(
				"http://localhost:8080/banking/ns/moneytransfer"));
		return new HttpEntity<AccountResource>(resource);
	}

	@RequestMapping("/account/{accountNumber}/transfer")
	public HttpEntity<FormDescriptor> moneyTransferForm(@PathVariable String accountNumber) {

		FormDescriptor form = createForm("moneyTransfer", on(BankingController.class).moneyTransfer(accountNumber, null, null));
		return new HttpEntity<FormDescriptor>(form);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/account/{accountNumber}/transfer", method = RequestMethod.POST)
	public HttpEntity<MoneyTransferResource> moneyTransfer(@PathVariable("accountNumber") String accountNumber,
			@RequestParam("to") String toAccountNo, @RequestParam("amount") BigInteger amount) {

		try {
			BankingConnection from = new BankingConnection(accountNumber);
			BankingConnection to = new BankingConnection(toAccountNo);
			MoneyTransfer moneyTransfer = moneyTransferAccess.createTransfer(from, to, amount);

			MoneyTransferResourceAssembler assembler = new MoneyTransferResourceAssembler();
			MoneyTransferResource resource = assembler.toResource(moneyTransfer);

			resource.add(new Link("http://example.com/ns/transfer", "describedBy"));

			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(resource.getId().getHref()));
			return new HttpEntity<MoneyTransferResource>(resource, headers);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
