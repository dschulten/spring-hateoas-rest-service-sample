package org.springframework.hateoas.client.hc;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.BrowsableRegistry;
import org.springframework.http.HttpHeaders;

@RunWith(MockitoJUnitRunner.class)
public class BrowsableResponseHandlerTest {

	BrowsableResponseHandler handler = new BrowsableResponseHandler();

	@Mock
	private BrowsableRegistry navigatorRegistry;

	@Mock
	private Browsable expectedBrowsable;

	@Before
	public void setUp() throws Exception {
		handler.setBrowsableRegistry(navigatorRegistry);
	}

	@Test
	public void testHandleResponseXhtml() throws ClientProtocolException, IOException {

		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		StringEntity entity = new StringEntity("important message", ContentType.create("text/xhtml", "UTF-8"));
		response.setEntity(entity);

		// expect
		when(navigatorRegistry.getBrowsable("text/xhtml")).thenReturn(expectedBrowsable);

		Browsable browsable = handler.handleResponse(response);
		assertSame(expectedBrowsable, browsable);
		verify(expectedBrowsable).process(Mockito.<InputStream> any(), Mockito.<HttpHeaders> any());
	}

	@Test
	public void testHandleResponseXForm() throws ClientProtocolException, IOException {

		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		StringEntity entity = new StringEntity("important message", ContentType.create("application/xhtml+xml", "UTF-8"));
		response.setEntity(entity);

		// expect
		when(navigatorRegistry.getBrowsable("application/xhtml+xml")).thenReturn(expectedBrowsable);

		Browsable navigator = handler.handleResponse(response);

		assertSame(expectedBrowsable, navigator);
		verify(expectedBrowsable).process(Mockito.<InputStream> any(), Mockito.<HttpHeaders> any());
	}

}
