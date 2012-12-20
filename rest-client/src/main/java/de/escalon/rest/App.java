package de.escalon.rest;

import static org.springframework.hateoas.client.Identifiers.describedBy;

import java.math.BigInteger;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.hateoas.client.Agent;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.Browser;
import org.springframework.hateoas.client.FollowRelAction;
import org.springframework.hateoas.client.GoalNotFoundException;
import org.springframework.hateoas.client.SubmitFormAction;
import org.springframework.hateoas.client.hc.CommonsHttpClient4Browser;
import org.springframework.hateoas.util.Args;

/**
 * Demo application for hypermedia api client.
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {
		URI uri = new URI("http://localhost:8080/");

		/**
		 * TODO: currently we support only the following json hypermedia format as implemented by JsonRelFinder.
		 * TODO: make RelFinders pluggable with a good default, should select RelFinder based on content-type
		 * collection+json, hal+json, json-ld
		 * <pre>
		 * {
		 * "links":[
		 * {
		 * "rel":"self",
		 * "href":"http://localhost:8080/people/1"
		 * },
		 * {
		 * "rel":"describedBy",
		 * "href":"http://example.com/doc#customer"
		 * }
		 * ],
		 * "firstname":"Thorin",
		 * "lastname":"Oakenshield1",
		 * "products":{
		 * "links":[
		 * {
		 * "rel":"describedBy",
		 * "href":"http://example.com/doc#products"
		 * }
		 * ],
		 * "content":[
		 * {
		 * "links":[
		 * {
		 * "rel":"self",
		 * "href":"http://localhost:8080/products/1"
		 * }
		 * ],
		 * "productName":"Starfleet Standard Tricorder"
		 * }
		 * ]
		 * }
		 * }
		 * </pre>
		 */
		// sensing if I only have a goal like moneytransferreceipt
		// use schema.org actions
		getProductsOfPerson(createBrowserForUri(uri), 1);
		getProductsOfPerson(createBrowserForUri(uri), 2);

		transferMoney(createBrowserForUri(new URI("http://localhost:8080/banking")), "123456", "111111", new BigInteger(
				"200"));

	}

	private static CommonsHttpClient4Browser createBrowserForUri(URI uri) {
		CommonsHttpClient4Browser browser = new CommonsHttpClient4Browser(uri);
		HttpClient httpClient = new DefaultHttpClient();
		browser.setHttpClient(httpClient);
		return browser;
	}

	private static void getProductsOfPerson(Browser browser, int personId) throws GoalNotFoundException {

		Agent agent = new Agent(browser);

		// order of follow rel actions is significant
		agent.addFollowRelAction(new FollowRelAction("search"));
		agent.addFollowRelAction(new FollowRelAction("products"));

		// enable client to submit form when encountered
		agent.addSubmitFormAction(new SubmitFormAction("searchPerson", Args.of("personId", personId)));

		agent.setGoal(describedBy("http://example.com/doc#product").within(describedBy("http://example.com/doc#customer")));
		Browsable result = agent.browseForGoal();

		System.out.println("\n--- result ---");
		System.out.println(result.toString());
		System.out.println("--------------\n");
	}

	/**
	 * Transfers money from one bank account to another. Uses rdf description of the current context. Looks for custom
	 * rels that are RDF IRIs as well. An rdf property becomes either a custom rel, if it must be retrieved through a
	 * hyperlink, or a describedBy rel, if its value is embedded in the current resource. If a public vocabulary for bank
	 * existed, it would allow us to get and cache that information, without any need to create our own rdf descriptor.
	 * 
	 * <pre>
	 * <!-- bank has an account property, with a transfer property -->
	 * <rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#">
	 *   <rdfs:Class rdf:about="http://localhost:8080/banking/ns/bank"/>
	 *   <rdf:Property rdf:about="http://localhost:8080/banking/ns/transfer">
	 *     <rdfs:domain>
	 *       <rdf:Property rdf:about="http://localhost:8080/banking/ns/account"/>
	 *     </rdfs:domain>
	 *     <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
	 *   </rdf:Property>
	 *   <rdf:Property rdf:about="http://localhost:8080/banking/ns/account">
	 *     <rdfs:domain rdf:resource="http://localhost:8080/banking/ns/bank"/>
	 *     <rdf:type rdf:resource="http://www.w3.org/2000/01/rdf-schema#Class"/>
	 *   </rdf:Property>
	 * </rdf:RDF>
	 * </pre>
	 * 
	 * @param browser
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @throws GoalNotFoundException 
	 */
	private static void transferMoney(Browser browser, String fromAccount, String toAccount, BigInteger amount) throws GoalNotFoundException {
		Agent agent = new Agent(browser);
		// collect follow rel actions from describedBy URL
		agent.identifyGoal("http://localhost:8080/banking/ns/transfer", browser.getCurrentContext());

		// enable client to submit forms
		// TODO: look into schema.org actions to see if they say something about required data for an action.
		agent.addSubmitFormAction(new SubmitFormAction("bankAccountForm", Args.of("accountNumber", fromAccount)));
		agent.addSubmitFormAction(new SubmitFormAction("moneyTransferForm", Args.of("amount", amount, "to", toAccount)));

		Browsable result = agent.browseForGoal();
		System.out.println("\n--- result ---");
		System.out.println(result.toString());
		System.out.println("--------------\n");
	}

}
