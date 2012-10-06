package org.springframework.hateoas.client.hc;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.hateoas.client.FormRequest;
import org.springframework.hateoas.client.BrowsableResource;
import org.springframework.hateoas.util.Args;
import org.springframework.http.HttpMethod;

@RunWith(MockitoJUnitRunner.class)
public class CommonsHttpClient4NavigatorTest {

    @Mock
    private HttpClient httpClient;
    @Mock
    BrowsableResource indexPage;
    @Mock
    BrowsableResource searchForm;
    @Mock
    BrowsableResource result;

    CommonsHttpClient4Navigator htmlNavigator;

    @Before
    public void setUp() throws Exception {
        URI uri = new URI("http://localhost:8080/");
        htmlNavigator = new CommonsHttpClient4Navigator(uri);
        htmlNavigator.setHttpClient(httpClient);
    }

    @Test
    public void testFollowRelAndSubmitForm() throws ClientProtocolException, IOException,
            URISyntaxException {
        Link expectedRel = new Link("/", "search");

        when(
                httpClient.execute(Mockito.<HttpGet> any(),
                        Mockito.<ResponseHandler<?>> any())).thenReturn(
                indexPage, searchForm, result);
        when(indexPage.getRel("search")).thenReturn(expectedRel);
        when(
                searchForm.getFormRequest(Mockito.eq("people"),
                        Mockito.<Args> any())).thenReturn(
                new FormRequest(HttpMethod.GET, "", new URI("/"), "", "UTF-8"));

        // TODO here I need a way of checking if I have what I am looking for,
        // not just browsing
        //
        htmlNavigator.followRel("search").submitForm("people",
                Args.from("customerId", 1));

        BrowsableResource currentResource = htmlNavigator.getCurrentResource();

        assertSame(result, currentResource);
    }

    @Test
    public void testSubmitForm() {
        fail("Not yet implemented");
    }

}
