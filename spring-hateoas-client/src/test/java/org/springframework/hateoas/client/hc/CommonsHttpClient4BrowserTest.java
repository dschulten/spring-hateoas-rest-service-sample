package org.springframework.hateoas.client.hc;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.FormRequest;
import org.springframework.hateoas.util.Args;
import org.springframework.http.HttpMethod;

@RunWith(MockitoJUnitRunner.class)
public class CommonsHttpClient4BrowserTest {

	@Mock
	private HttpClient httpClient;
	@Mock
	Browsable indexPage;
	@Mock
	Browsable searchForm;
	@Mock
	Browsable result;

	CommonsHttpClient4Browser browser;

	@Before
	public void setUp() throws Exception {
		URI uri = new URI("http://localhost:8080/");
		browser = new CommonsHttpClient4Browser(uri);
		browser.setHttpClient(httpClient);
	}

	@Test
	public void testFollowRelAndSubmitForm() throws ClientProtocolException, IOException, URISyntaxException {
		Link expectedRel = new Link("/", "search");

		when(httpClient.execute(Mockito.<HttpGet> any(), Mockito.<ResponseHandler<?>> any())).thenReturn(indexPage,
				searchForm, result);
		when(indexPage.getRel("search")).thenReturn(expectedRel);
		when(searchForm.getFormRequest(Mockito.eq("people"), Mockito.<Args> any())).thenReturn(
				new FormRequest(HttpMethod.GET, "", new URI("/"), "", "UTF-8"));

		// TODO here I need a way of checking if I have what I am looking for,
		// not just browsing
		//
		browser.followRel("search").submitForm("people", Args.of("customerId", 1));

		Browsable currentResource = browser.getCurrentResource();

		assertSame(result, currentResource);
	}

}
