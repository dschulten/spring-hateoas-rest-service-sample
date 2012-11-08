package de.escalon.rest;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.Browser;
import org.springframework.hateoas.client.DescribedByIdentifier;
import org.springframework.hateoas.client.FollowRelAction;
import org.springframework.hateoas.client.Identifier;
import org.springframework.hateoas.client.Inventory;
import org.springframework.hateoas.client.SubmitFormAction;
import org.springframework.hateoas.client.hc.CommonsHttpClient4Browser;
import org.springframework.hateoas.util.Args;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {
		URI uri = new URI("http://localhost:8080/");

		getProductsOfPerson(createBrowserForUri(uri), 1);
		getProductsOfPerson(createBrowserForUri(uri), 2);

	}

	private static CommonsHttpClient4Browser createBrowserForUri(URI uri) {
		CommonsHttpClient4Browser browser = new CommonsHttpClient4Browser(uri);
		HttpClient httpClient = new DefaultHttpClient();
		browser.setHttpClient(httpClient);
		return browser;
	}

	private static void getProductsOfPerson(Browser browser, int personId) {

		Inventory inventory = new Inventory();
		inventory.addAction(new FollowRelAction("search"));
		inventory.addAction(new SubmitFormAction("searchPerson", Args.of("personId", personId)));
		inventory.addAction(new FollowRelAction("products"));

		// TODO consider browseFor, should it be able to return an object containing
		// the described thing - in this case a customer with her products and details?
		// a visitor comes to mind which assembles the result
		Browsable result = browser
				.browseFor(
						describedBy("http://example.com/doc#products").within(describedBy("http://example.com/doc#customer")),
						inventory);

		System.out.println("\n--- result ---");
		System.out.println(result.toString());
		System.out.println("--------------\n");
	}

	private static Identifier describedBy(String descriptor) {
		// TODO Auto-generated method stub
		return new DescribedByIdentifier(descriptor);
	}
}
