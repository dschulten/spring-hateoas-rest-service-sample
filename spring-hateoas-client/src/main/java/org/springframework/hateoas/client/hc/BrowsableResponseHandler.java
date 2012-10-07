package org.springframework.hateoas.client.hc;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.hateoas.client.Browsable;
import org.springframework.hateoas.client.BrowsableRegistry;
import org.springframework.http.HttpHeaders;

public class BrowsableResponseHandler implements
        ResponseHandler<Browsable> {

    private BrowsableRegistry navigatorRegistry;

    public Browsable handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {

        HttpEntity entity = response.getEntity();
        Header contentType = entity.getContentType();
        HeaderElement[] elements = contentType.getElements();
        if (elements.length != 1) {
            throw new IllegalArgumentException(
                    "more than one response mime type currently not supported");
        }
        String mimeType = elements[0].getName();

        Browsable browsable = navigatorRegistry.getBrowsable(mimeType);

        // navigator.setURL(response.);

        HttpHeaders httpHeaders = toHttpHeaders(response.getAllHeaders());

        browsable.process(entity.getContent(), httpHeaders);

        return browsable;

    }

    private HttpHeaders toHttpHeaders(Header[] allHeaders) {
        HttpHeaders ret = new HttpHeaders();
        for (Header header : allHeaders) {
            HeaderElement[] elements = header.getElements();
            ret.add(header.getName(), header.getValue());
        }
        return ret;
    }

    public BrowsableRegistry getNavigatorRegistry() {
        return navigatorRegistry;
    }

    @Required
    public void setNavigatorRegistry(BrowsableRegistry navigatorRegistry) {
        this.navigatorRegistry = navigatorRegistry;
    }

}
