package org.springframework.hateoas.client.hc;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.io.IOException;

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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.client.BrowsableResource;
import org.springframework.hateoas.client.Navigator;
import org.springframework.hateoas.client.NavigatorRegistry;
import org.springframework.hateoas.client.XFormsNavigable;
import org.springframework.hateoas.client.XHtmlNavigable;
import org.springframework.http.HttpHeaders;

@RunWith(MockitoJUnitRunner.class)
public class NavigableResponseHandlerTest {

    NavigableResponseHandler handler = new NavigableResponseHandler();

    @Mock
    private NavigatorRegistry navigatorRegistry;

    @Before
    public void setUp() throws Exception {
        handler.setNavigatorRegistry(navigatorRegistry);
    }

    @Test
    public void testHandleResponseXhtml() throws ClientProtocolException,
            IOException {

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        StringEntity entity = new StringEntity("important message",
                ContentType.create("text/xhtml", "UTF-8"));
        response.setEntity(entity);

        // expect
        XHtmlNavigable expectedNavigator = new XHtmlNavigable();
        when(navigatorRegistry.getNavigator("text/xhtml")).thenReturn(
                expectedNavigator);

        BrowsableResource navigator = handler.handleResponse(response);
        assertSame(expectedNavigator, navigator);
    }

    @Test
    public void testHandleResponseXForm() throws ClientProtocolException,
            IOException {

        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
                HttpStatus.SC_OK, "OK");
        StringEntity entity = new StringEntity("important message",
                ContentType.create("application/xhtml+xml", "UTF-8"));
        response.setEntity(entity);

        // expect
        XHtmlNavigable expectedNavigator = new XFormsNavigable();
        when(navigatorRegistry.getNavigator("application/xhtml+xml"))
                .thenReturn(expectedNavigator);

        BrowsableResource navigator = handler.handleResponse(response);
        assertSame(expectedNavigator, navigator);
    }

}
