package de.escalon.rest;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.hateoas.client.Browser;
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

		browser.followRel("search")
			.submitForm("searchPerson", Args.of("personId", personId))
			.followRel("products");
		System.out.println("\n--- result ---");
		System.out.println(browser.getCurrentResource().toString());
		System.out.println("--------------\n");
	}
}
